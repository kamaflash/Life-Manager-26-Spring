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
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

/**
 *
 * @author Pc
 */
@Configuration
@EnableWebSecurity
public class SpringSecurity {

    // Todas las URLs del sistema
    private static final String[] ALL_URLS = {
            // URLs existentes
            "/api/character-jobs/**",
            "/api/companies/**",
            "/api/job-applications/**",
            "/api/job-events/**",
            "/api/job-vacancies/**",
            "/api/work-relationships/**",
            "/api/job-positions/**",
            "/api/missions/**",
            "/api/payrolls/**",
            // URLs del sistema de IRPF
            "/api/tax/withholdings/**",
            "/api/tax/filings/**",
            "/api/tax/periods/**",
            "/api/tax/notifications/**",
            "/api/tax/dashboard/**"
    };

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
                        .requestMatchers("/h2-console/**").permitAll()

                        // GET - Permitidos
                        .requestMatchers(HttpMethod.GET, ALL_URLS).permitAll()

                        // POST - Permitidos
                        .requestMatchers(HttpMethod.POST, ALL_URLS).permitAll()

                        // PUT - Permitidos
                        .requestMatchers(HttpMethod.PUT, ALL_URLS).permitAll()

                        // DELETE - Permitidos
                        .requestMatchers(HttpMethod.DELETE, ALL_URLS).permitAll()

                        .anyRequest().authenticated()
                )
                .build();
    }
}