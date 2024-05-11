package com.tabwu.door.aop;

import com.tabwu.door.service.DoorService;
import com.tabwu.door.annotation.Door;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/16 15:14
 * @DESCRIPTION:
 */
@Aspect
@Component
public class DoorAspect {

    @Autowired
    private DoorService doorService;

    @Pointcut("@annotation(com.tabwu.door.annotation.Door)")
    public void aopPoint() {}

    @Around("aopPoint()")
    public void around(ProceedingJoinPoint pj) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            Method method = ((MethodSignature) pj.getSignature()).getMethod();
            Annotation doorAnnotation = method.getDeclaredAnnotation(Door.class);
            if (doorAnnotation == null) {
                Object result = pj.proceed();
            }
            String remoteAddr = request.getRemoteAddr();
            if (!doorService.contains(remoteAddr)) {
                throw new Exception("你的ip:[" + remoteAddr + "]还未加入服务列表，如已购买服务，请联系服务商...");
            } else {
                Object result = pj.proceed();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
