package com.tabwu.door.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/5/8 13:22
 * @DESCRIPTION:
 */
@ConfigurationProperties(prefix = "wu.encrypt")
public class EncryptProperty {

    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
