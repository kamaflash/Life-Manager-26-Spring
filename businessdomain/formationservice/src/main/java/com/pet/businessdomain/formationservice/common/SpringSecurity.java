/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.formationservice.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 *
 * @author Pc
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {
    public static final String URL = "/api/formations/**";
    public static final String URLTRAINER = "/api/trainer/**";
    public static final String URLSCHOL = "/api/scholarships/**";
    public static final String URLEXAM = "/api/exams/**";
    public static final String URLEXAMC = "/api/character-exams/**";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                        ))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // ✅ AÑADIR ESTO: Permitir acceso a la consola H2
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, URL, URLTRAINER, URLSCHOL, URLEXAM, URLEXAMC).permitAll()
                        .requestMatchers(HttpMethod.POST, URL,URLTRAINER, URLSCHOL, URLEXAM, URLEXAMC).permitAll()
                        .requestMatchers(HttpMethod.PUT, URL,URLTRAINER, URLSCHOL, URLEXAM, URLEXAMC).permitAll()
                        .requestMatchers(HttpMethod.DELETE, URL,URLTRAINER, URLSCHOL, URLEXAM, URLEXAMC).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}

