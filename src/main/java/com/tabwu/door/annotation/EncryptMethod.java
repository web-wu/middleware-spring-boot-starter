package com.tabwu.door.annotation;

import com.tabwu.door.constant.EncryptType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/5/8 13:16
 * @DESCRIPTION:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EncryptMethod {

    EncryptType type() default EncryptType.IN;

    Class<?> clazz();

}
