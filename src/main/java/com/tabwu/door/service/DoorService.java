package com.tabwu.door.service;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/16 15:04
 * @DESCRIPTION:
 */
@Component
public class DoorService {

    private String hosts;

    private static final Set<String> JOST_SET = new HashSet<>();

    public DoorService(String hosts) {
        this.hosts = hosts;
        for (String host : hosts.split(",")) {
            JOST_SET.add(host.trim());
            System.out.println("加载配置host---" + host.trim());
        }
    }

    public Boolean contains(String remoteHost) {
        return JOST_SET.contains(remoteHost);
    }


    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }
}
