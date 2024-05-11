package com.tabwu.door.constant;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/5/9 16:04
 * @DESCRIPTION:
 */
public enum EncryptType {
    IN("数据入库加密"),
    OUT("数据出库解密");

    private String descrption;

    EncryptType(String descrption) {
        this.descrption = descrption;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }
}
