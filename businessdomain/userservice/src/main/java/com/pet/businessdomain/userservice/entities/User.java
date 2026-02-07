/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.userservice.entities;

import com.pet.businessdomain.userservice.dto.CharacterDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 *
 * @author Pc
 */
@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean status = false;

    @Transient
    private CharacterDto persons;



}