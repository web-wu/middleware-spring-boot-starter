package com.tabwu.door.service;

import com.tabwu.door.annotation.RateLimiter;
import com.tabwu.door.constant.LimitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/25 15:15
 * @DESCRIPTION:
 */
public class RateLimiterService {

    private final String sufixKey;

    public RateLimiterService(String sufixKey) {
        this.sufixKey = sufixKey;
    }

    @Autowired
    private StringRedisTemplate redisTemplate;


    public boolean checkRateLimiter(String remoteAddr, RateLimiter rateLimiterAnnotation, Method method) {
        String redisKey = createKey(remoteAddr, method, rateLimiterAnnotation.limit_type());
        long currentTime = System.currentTimeMillis();
        long endTime = currentTime - rateLimiterAnnotation.time_window();

        // redisKey 存在判断与 过期时间 s 设置的原子性操作
        String script ="local endTime = tonumber(ARGV[1]) " +
                "local count = tonumber(ARGV[2]) " +
                "local currentTime = tonumber(ARGV[3]) " +
                "local windowTime = tonumber(ARGV[4]) " +
                "redis.call('zremrangebyscore', KEYS[1], 0, endTime) " +
                "if redis.call('zcard', KEYS[1]) > count then return 0 " +
                "else redis.call('zadd', KEYS[1], currentTime, currentTime) redis.call('expire', KEYS[1], windowTime) return 1 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(redisKey);
        Long result = (Long) redisTemplate.execute(redisScript, keyList, String.valueOf(endTime), String.valueOf(rateLimiterAnnotation.count()), String.valueOf(currentTime), String.valueOf(rateLimiterAnnotation.time_window() / 1000));
        // redisKey 大于 5 返回 0 --- true
        return result != null && result == 0L;
    }

    private String createKey(String remoteAddr, Method method, LimitType limit_type) {
        StringBuilder stringBuilder = new StringBuilder(sufixKey).append(":");
        if (limit_type == LimitType.IP) {
            stringBuilder.append(remoteAddr).append(":");
        }
        Class<?> methodDeclaringClass = method.getDeclaringClass();
        stringBuilder.append(methodDeclaringClass.getName()).append(".").append(method.getName());
        return stringBuilder.toString();
    }
}
