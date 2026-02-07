/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.financeservice.common;

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
    public static final String URLACCOUNT = "/api/accounts/**";
    public static final String URLEXPENSES = "/api/expenses/**";
    public static final String URLINCOMES = "/api/incomes/**";
    public static final String URLTRANS = "/api/transactions/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, URLACCOUNT, URLEXPENSES, URLINCOMES, URLTRANS).permitAll()
                        .requestMatchers(HttpMethod.POST, URLACCOUNT, URLEXPENSES, URLINCOMES, URLTRANS).permitAll()
                        .requestMatchers(HttpMethod.PUT, URLACCOUNT, URLEXPENSES, URLINCOMES, URLTRANS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, URLACCOUNT, URLEXPENSES, URLINCOMES, URLTRANS).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}

