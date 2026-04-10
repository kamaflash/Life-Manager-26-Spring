package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.JobEventDTO;

import java.util.List;

public interface JobEventService {

    // ===== CRUD BÁSICO =====
    JobEventDTO getById(Long id);
    List<JobEventDTO> getByCharacterJob(Long characterJobId);
    JobEventDTO create(JobEventDTO dto);
    // ===== GENERACIÓN DE EVENTOS =====
    JobEventDTO generateRandomEvent(Long characterJobId);
    JobEventDTO generateBonusEvent(Long characterJobId, Integer amount);
    JobEventDTO generatePromotionEvent(Long characterJobId);
    JobEventDTO generateConflictEvent(Long characterJobId, Long withCharacterId);
    JobEventDTO generateProjectSuccessEvent(Long characterJobId, Integer xpReward);
    JobEventDTO generateProjectFailureEvent(Long characterJobId);
    JobEventDTO generateBurnoutEvent(Long characterJobId);

    // ===== PROCESAMIENTO =====
    void resolveEvent(Long eventId);
    void applyEventEffects(Long characterJobId, JobEventDTO event);

    // ===== LISTADOS =====
    List<JobEventDTO> getPositiveEvents(Long characterJobId);
    List<JobEventDTO> getNegativeEvents(Long characterJobId);
    List<JobEventDTO> getRecentEvents(Long characterJobId, int limit);

    // ===== ESTADÍSTICAS =====
    Long countEventsByType(Long characterJobId, String eventType);
}