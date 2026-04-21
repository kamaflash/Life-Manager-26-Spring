package com.pet.businessdomain.shareddto.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CharacterDto {

    private Long id;
    private Long uid;

    private String name;
    private String avatar;

    private Integer age;
    private LocalDateTime birthDate;
    private String gender;

    private BigDecimal newIncome;
    private BigDecimal newExpense;
    private String city;
    private String residentialCity;
    private StatsDto stats;
    private Integer xpAcademy;
    private Integer xpJobs;
    private Integer level;
    private Map<String, SkillStateDto> skills;

    private List<CharacterTrainingDto> education;
    private List<CharacterJobDTO> jobs;
    private List<SocialExperienceDto> social;

    private List<String> interests;
    private List<String> friends;

    private String partnerId;
    private List<String> familyIds;

    private List<CharacterInventoryResponseDTO> inventory;
    // ===== NUEVOS CAMPOS PARA MISIÓN/EVENTOS =====
    private List<Long> activeMissionIds;           // IDs de CharacterMissionRecord activos
    private List<String> completedMissionCodes;    // Para chequeos rápidos de requisitos
    private List<String> titles;                   // Títulos ganados: "Graduado", "Empresario"
    private String activeTitle;                    // Título equipado actualmente
    private List<String> badges;

    private String createdAt;
    private String updatedAt;
    private List<SFinanceAccountResponseDto> accounts;
}
