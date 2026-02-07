package com.pet.businessdomain.financeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class FinanceserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanceserviceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalanceWebClientBuilder() {
        return WebClient.builder();
    }
}
