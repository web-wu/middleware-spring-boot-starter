package com.tabwu.door.aop;

import com.alibaba.fastjson.JSON;
import com.tabwu.door.annotation.Idempotent;
import com.tabwu.door.service.IdempotentService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/19 15:27
 * @DESCRIPTION:
 */
@Aspect
@Component
@ConditionalOnBean(value = {IdempotentService.class})
public class IdempotentAspect {

    @Autowired
    private IdempotentService idempotentService;

    @Pointcut("@annotation(com.tabwu.door.annotation.Idempotent)")
    public void aopPointcut() {}

    @Around("aopPointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Idempotent idempotentAnnotation = method.getDeclaredAnnotation(Idempotent.class);
        if (idempotentAnnotation == null) {
            return pjp.proceed();
        }
        //传入的 参数值 数组
        Object[] args = pjp.getArgs();
        //方法的 参数名 数组
        String[] argsNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
        HashMap<String, Object> paramters = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            paramters.put(argsNames[i],args[i]);
        }
        String paramsJson = JSON.toJSONString(paramters);
        String methodName = method.getName();

        if (idempotentService.checkIdempotent(methodName, paramsJson, idempotentAnnotation.key(), idempotentAnnotation.expiration())) {
            throw new Exception("方法【" + methodName + "】幂等性检测未通过！");
        }
        return pjp.proceed();
    }
}


