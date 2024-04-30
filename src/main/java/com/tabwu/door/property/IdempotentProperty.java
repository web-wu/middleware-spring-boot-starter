package com.tabwu.door.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/19 14:58
 * @DESCRIPTION:
 */
@ConfigurationProperties(prefix = "wu.idempotent")
public class IdempotentProperty {
    private boolean enabled;
    private String key;
    private int expiration;


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
