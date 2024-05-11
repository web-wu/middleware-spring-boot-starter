package com.tabwu.door.service;

import com.tabwu.door.annotation.EncryptField;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/5/8 13:24
 * @DESCRIPTION: 针对不同字段的加密策略需要在 encryptField 中自定义
 */
public class EncryptService {

    private final String key = "25d55ad283aa400af464c76d713c07ad";

    public void encrypt(Object[] args, Class<?> objClazz) {
        Arrays.stream(args).forEach(arg -> {
            System.out.println("3...");
            if (arg.getClass().isAssignableFrom(objClazz)) {
                System.out.println("4...");
                Field[] fields = arg.getClass().getDeclaredFields();
                Arrays.stream(fields).forEach(field -> {
                    if (field.isAnnotationPresent(EncryptField.class)) {
                        System.out.println("5...");
                        // EncryptField encryptField = field.getAnnotation(EncryptField.class);
                        field.setAccessible(true);
                        try {
                            field.set(arg, encryptByAES(field.get(arg).toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public Object decrypt(Object res, Class<?> objClazz) {
        if (res.getClass().isAssignableFrom(objClazz)) {
            Field[] fields = res.getClass().getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                if (field.isAnnotationPresent(EncryptField.class)) {
                    // EncryptField encryptField = field.getAnnotation(EncryptField.class);
                    field.setAccessible(true);
                    try {
                        field.set(res, decryptByAES(field.get(res).toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return res;
    }

    private String encryptByAES(String fieldValue) throws Exception {
        System.out.println("6...");
        // 算法
        String algorithm = "AES";
        String transformation = "AES";
        // Cipher：密码，获取加密对象
        // transformation:参数表示使用什么类型加密
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定秘钥规则
        // 第一个参数表示：密钥，key的字节数组 长度必须是16位
        // 第二个参数表示：算法
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        // 对加密进行初始化
        // 第一个参数：表示模式，有加密模式和解密模式
        // 第二个参数：表示秘钥规则
        cipher.init(Cipher.ENCRYPT_MODE,sks);
        // 进行加密
        byte[] bytes = cipher.doFinal(fieldValue.getBytes());
        return bytes.toString();
    }

    private String decryptByAES(String input)throws Exception{
        // 算法
        String algorithm = "AES";
        String transformation = "AES";
        // Cipher：密码，获取加密对象
        // transformation:参数表示使用什么类型加密
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定秘钥规则
        // 第一个参数表示：密钥，key的字节数组 长度必须是16位
        // 第二个参数表示：算法
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        // 对加密进行初始化
        // 第一个参数：表示模式，有加密模式和解密模式
        // 第二个参数：表示秘钥规则
        cipher.init(Cipher.DECRYPT_MODE,sks);
        // 进行解密
        byte [] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        byte[] bytes = cipher.doFinal(inputBytes);
        return new String(bytes);
    }

}
