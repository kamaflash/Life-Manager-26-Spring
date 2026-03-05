/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.jobservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Pc
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {
    public static final String URLACCOUNT = "/api/notifications/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, URLACCOUNT).permitAll()
                        .requestMatchers(HttpMethod.POST, URLACCOUNT).permitAll()
                        .requestMatchers(HttpMethod.PUT, URLACCOUNT).permitAll()
                        .requestMatchers(HttpMethod.DELETE, URLACCOUNT).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}

