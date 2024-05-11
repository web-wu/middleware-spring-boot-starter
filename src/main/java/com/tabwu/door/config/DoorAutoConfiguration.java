package com.tabwu.door.config;

import com.tabwu.door.property.DoorProperty;
import com.tabwu.door.property.EncryptProperty;
import com.tabwu.door.property.IdempotentProperty;
import com.tabwu.door.property.RateLimitterProperty;
import com.tabwu.door.service.DoorService;
import com.tabwu.door.service.EncryptService;
import com.tabwu.door.service.IdempotentService;
import com.tabwu.door.service.RateLimiterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @PROJECT_NAME: door-spring-boot-starter
 * @USER: tabwu
 * @DATE: 2024/4/16 13:23
 * @DESCRIPTION:
 */
@Configuration
@EnableConfigurationProperties({DoorProperty.class, IdempotentProperty.class, RateLimitterProperty.class, EncryptProperty.class})
// 扫描aop切面类，加载到spring容器中
@ComponentScan(basePackages = "com.tabwu.door.aop")
public class DoorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "wu.door", value = "enabled", havingValue = "true")
    public DoorService doorService(DoorProperty property) {
        return new DoorService(property.getHost());
    }

    @Bean
    @ConditionalOnClass(value = {RedisTemplate.class, RedisProperties.Jedis.class})
    @ConditionalOnProperty(prefix = "wu.idempotent", value = "enabled", havingValue = "true")
    public IdempotentService idempotentService(IdempotentProperty idempotentProperty) {
        return new IdempotentService(idempotentProperty.getKey(), idempotentProperty.getExpiration());
    }

    @Bean
    @ConditionalOnClass(value = {RedisTemplate.class, RedisProperties.Jedis.class})
    @ConditionalOnProperty(prefix = "wu.limiter", value = "enabled", havingValue = "true")
    public RateLimiterService rateLimiterService(RateLimitterProperty rateLimitterProperty) {
        return new RateLimiterService(rateLimitterProperty.getKey());
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "wu.encrypt", value = "enabled", havingValue = "true")
    public EncryptService encryptService() {
        return new EncryptService();
    }

}
