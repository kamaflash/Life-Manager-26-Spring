package com.pet.businessdomain.eventservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.function.client.WebClient;

@ComponentScan(basePackages = {
        "com.pet.businessdomain.eventservice",  // Solo eventservice
})
@SpringBootApplication
public class EventserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventserviceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalanceWebClientBuilder() {
        return WebClient.builder();
    }
}