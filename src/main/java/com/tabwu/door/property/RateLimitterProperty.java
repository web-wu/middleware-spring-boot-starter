package com.tabwu.door.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/25 15:16
 * @DESCRIPTION:
 */
@ConfigurationProperties(prefix = "wu.limiter")
public class RateLimitterProperty {

    private String key = "ratelimiter";

    private Boolean enabled;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
