package com.pet.businessdomain.userservice.dto;


import lombok.Data;

@Data
public class RelationshipDto {
    private String id;
    private String personId;
    private String type; // "friend" | "partner" | "family" | "colleague"
    private Integer closeness; // 0-100
    private Integer trust;     // 0-100
    private Integer interactionCount; // opcional
}
