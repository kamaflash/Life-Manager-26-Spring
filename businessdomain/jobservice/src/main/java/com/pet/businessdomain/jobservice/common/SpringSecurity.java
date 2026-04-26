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
    public static final String URLCHARACTERJOB = "/api/character-jobs/**";
    public static final String URLCOMPANY = "/api/companies/**";
    public static final String URLJOBAPP = "/api/job-applications/**";
    public static final String URLJOBEVENT = "/api/job-events/**";
    public static final String URLJOBVACANCIES = "/api/job-vacancies/**";
    public static final String URLWORK = "/api/work-relationships/**";
    public static final String URLJOBPOS = "/api/job-positions/**";
    public static final String URLMISSIONS = "/api/missions/**";
    public static final String URLPAYROLLS = "/api/payrolls/**";


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
                        .requestMatchers(HttpMethod.GET, URLCHARACTERJOB, URLCOMPANY, URLJOBAPP, URLJOBEVENT, URLJOBVACANCIES, URLWORK, URLJOBPOS, URLMISSIONS, URLPAYROLLS).permitAll()
                        .requestMatchers(HttpMethod.POST, URLCHARACTERJOB, URLCOMPANY, URLJOBAPP, URLJOBEVENT, URLJOBVACANCIES, URLWORK, URLJOBPOS, URLMISSIONS, URLPAYROLLS).permitAll()
                        .requestMatchers(HttpMethod.PUT, URLCHARACTERJOB, URLCOMPANY, URLJOBAPP, URLJOBEVENT, URLJOBVACANCIES, URLWORK, URLJOBPOS, URLMISSIONS, URLPAYROLLS).permitAll()
                        .requestMatchers(HttpMethod.DELETE, URLCHARACTERJOB, URLCOMPANY, URLJOBAPP, URLJOBEVENT, URLJOBVACANCIES, URLWORK, URLJOBPOS, URLMISSIONS, URLPAYROLLS).permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}

