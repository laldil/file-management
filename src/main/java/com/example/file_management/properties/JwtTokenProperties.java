package com.example.file_management.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author aldi
 * @since 18.06.2024
 */

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenProperties {
    private String secret;
    private Duration lifetime;
}
