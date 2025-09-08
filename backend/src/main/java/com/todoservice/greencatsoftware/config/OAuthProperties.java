package com.todoservice.greencatsoftware.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "oauth2")
@Getter
@Setter
public class OAuthProperties {
    @Data
    public static class Provider {
        private String restApiKey;
        private String redirectUri;
    }

    private Map<String, Provider> providers;
}
