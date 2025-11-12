package com.example.edcadmin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${app.edc.baseUrl}")
    private String edcBaseUrl;

    @Value("${app.edc.consumerBaseUrl}")
    private String consumerBaseUrl;

    @Bean
    public WebClient edcClient() {
        return WebClient.builder()
                .baseUrl(edcBaseUrl)
                // Don't set default Content-Type - let it be set per request
                .build();
    }

    @Bean
    public WebClient consumerClient() {
        return WebClient.builder()
                .baseUrl(consumerBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
