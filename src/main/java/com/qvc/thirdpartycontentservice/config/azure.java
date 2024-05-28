package com.qvc.thirdpartycontentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class azure {


    @Value("${token.access}")
    private String accessToken;

    @Value("${token.refresh}")
    private String refreshToken;

    // Getters and setters
}
