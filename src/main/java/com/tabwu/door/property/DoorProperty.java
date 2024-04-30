package com.tabwu.door.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/16 15:00
 * @DESCRIPTION:
 */
@ConfigurationProperties(prefix = "wu.door")
public class DoorProperty {
    private Boolean enabled;
    private String host;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
