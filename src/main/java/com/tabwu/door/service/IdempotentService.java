package com.tabwu.door.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/19 15:07
 * @DESCRIPTION:
 */
public class IdempotentService {
    private String key;
    private int expiration;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public IdempotentService(String key, int expiration) {
        this.key = key;
        this.expiration = expiration;
    }

    public boolean checkIdempotent(String methodName, String paramsJson, String inputKey, int inputExpiration) throws Exception {
        this.key = inputKey == null || inputKey == "" ? key : inputKey;
        this.expiration = inputExpiration == 0 ? expiration : inputExpiration;
        String paramsMd5 = this.dedupMd5Hander(paramsJson);
        String redisKey = this.key + ":m=" + methodName + ":p=" + paramsMd5;
        // redisKey 存在判断与 过期时间 s 设置的原子性操作
        String script = "if redis.call('get', KEYS[1]) == false " +
                "then redis.call('set', KEYS[1], ARGV[1]) redis.call('expire', KEYS[1], ARGV[2]) return 0 " +
                "else return 1 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(Long.class);
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(redisKey);
        Long result = redisTemplate.execute(redisScript, keyList, "idempotent_value", this.expiration + "");
        // redisKey 存在时返回1 --- true
        return result != null && result != 0L;
    }


    // 对参数进行MD5摘要
    private String dedupMd5Hander(String argJson) throws Exception {
        String str = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(argJson.getBytes(StandardCharsets.UTF_8));
            str = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("对方法参数进行MD5摘要时发生异常");
        }
        return str;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}
