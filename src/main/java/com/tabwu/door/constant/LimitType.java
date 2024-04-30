package com.tabwu.door.constant;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/25 15:01
 * @DESCRIPTION:
 */
public enum LimitType {

    IP(),
    METHOD();

    private String ip;

    private String method;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
