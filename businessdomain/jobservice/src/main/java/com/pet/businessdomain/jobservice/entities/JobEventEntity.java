package com.pet.businessdomain.jobservice.entities;

import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_events")
@Data
public class JobEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long characterJobId;

    @Enumerated(EnumType.STRING)
    private EnumAll.EventType type; // BONUS, PROMOTION, CONFLICT, PROJECT_SUCCESS, BURNOUT

    private String title;
    private String description;

    // Efectos del evento
    private Integer salaryChange; // positivo o negativo
    private Integer performanceChange;
    private Integer satisfactionChange;
    private Integer stressChange;

    private LocalDateTime occurredAt;
    private Boolean resolved = false;
}
