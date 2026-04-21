package com.pet.businessdomain.shareddto.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayEventDTO {
    private String type;          // "study", "work", "rest", "event", "exam", etc.
    private String title;         // Título del evento
    private String description;   // Descripción detallada
    private String icon;          // Icono para frontend (opcional)
    private String color;         // Color para frontend (opcional)

    // Cambios en estadísticas
    private int energyGain;
    private int stressReduction;
    private int moneyEarned;
    private int xpGained;

    // Habilidades desbloqueadas
    private java.util.List<String> skillsUnlocked;

    // Items recibidos
    private java.util.List<String> itemsReceived;

    // Metadata adicional
    private java.util.Map<String, Object> metadata;
}
