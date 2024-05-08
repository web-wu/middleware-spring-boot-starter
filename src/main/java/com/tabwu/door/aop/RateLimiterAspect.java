package com.tabwu.door.aop;

import com.tabwu.door.annotation.RateLimiter;
import com.tabwu.door.service.RateLimiterService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/25 15:30
 * @DESCRIPTION:
 */
@Component
@Aspect
@ConditionalOnBean(value = {RateLimiterService.class})
public class RateLimiterAspect {

    @Autowired
    private RateLimiterService rateLimiterService;


    @Pointcut("@annotation(com.tabwu.door.annotation.RateLimiter)")
    public void aopPointcut() {}

    @Before("aopPointcut()")
    public void doBefore(JoinPoint jp) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String remoteAddr = request.getRemoteAddr();
        Method method = ((MethodSignature) jp.getSignature()).getMethod();
        RateLimiter rateLimiterAnnotation = method.getDeclaredAnnotation(RateLimiter.class);
        if (rateLimiterAnnotation != null && rateLimiterService.checkRateLimiter(remoteAddr, rateLimiterAnnotation, method)) {
            throw new RuntimeException("请求过于频繁，请稍后再试");
        }
    }
}
