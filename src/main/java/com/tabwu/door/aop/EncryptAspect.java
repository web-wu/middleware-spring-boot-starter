package com.tabwu.door.aop;

import com.tabwu.door.annotation.EncryptMethod;
import com.tabwu.door.constant.EncryptType;
import com.tabwu.door.service.EncryptService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/5/8 13:30
 * @DESCRIPTION:
 */
@Component
@Aspect
public class EncryptAspect {

    @Autowired
    private EncryptService encryptService;

    @Pointcut("@annotation(com.tabwu.door.annotation.EncryptMethod)")
    public void doPointcut() {}

    @Around("doPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) {
        System.out.println("0...");
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        EncryptMethod encryptMethodAnnotation = method.getDeclaredAnnotation(EncryptMethod.class);
        if (encryptMethodAnnotation == null) {
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        System.out.println("1...");
        Class<?> objClazz = encryptMethodAnnotation.clazz();

        assert objClazz != null : "clazz type must not be null";

        Object res = null;

        // 数据入库加密
        if (encryptMethodAnnotation.type() == EncryptType.IN) {
            System.out.println("2...");
            //传入的 参数值 数组
            Object[] args = pjp.getArgs();
            encryptService.encrypt(args, objClazz);
        }

        try {
            res = pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // 查询数据出库解密
        if (res != null && encryptMethodAnnotation.type() == EncryptType.OUT) {
            res = encryptService.decrypt(res, objClazz);
        }

        return res;
    }
}
