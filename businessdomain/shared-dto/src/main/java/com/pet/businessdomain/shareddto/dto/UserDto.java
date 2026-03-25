/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pet.businessdomain.shareddto.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 *
 * @author Pc
 */
@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean status;

    private CharacterDto persons;
}
