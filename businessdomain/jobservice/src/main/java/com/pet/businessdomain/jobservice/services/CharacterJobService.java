package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterJobDTO;
import com.pet.businessdomain.shareddto.dto.WorkProgressDTO;

import java.util.List;

public interface CharacterJobService {

    // ===== CRUD BÁSICO =====
    CharacterJobDTO startJob(Long applicationId);
    CharacterJobDTO getById(Long id);
    CharacterJobDTO getCurrentJob(Long characterId);
    void resign(Long id);
    void terminate(Long id, String reason);

    // ===== LISTADOS =====
    List<CharacterJobDTO> getByCharacter(Long characterId);
    List<CharacterJobDTO> getActiveByCharacter(Long characterId);
    List<CharacterJobDTO> getHistoryByCharacter(Long characterId);
    List<CharacterJobDTO> getByCompany(Long companyId);

    // ===== ACCIONES LABORALES =====
    WorkProgressDTO workDay(Long characterJobId, Integer hoursWorked);
    WorkProgressDTO studyOnJob(Long characterJobId);
    WorkProgressDTO requestPromotion(Long characterJobId);
    WorkProgressDTO requestRaise(Long characterJobId);

    // ===== ESTADÍSTICAS Y PROGRESO =====
    void updatePerformance(Long id, Integer performance);
    void updateSatisfaction(Long id, Integer satisfaction);
    void updateStress(Long id, Integer stressChange);
    void addPromotion(Long id);
    void addBonus(Long id, Integer amount);

    // ===== VERIFICACIONES =====
    boolean hasActiveJob(Long characterId);
    boolean canWorkToday(Long characterJobId);
    boolean canRequestPromotion(Long characterJobId);
}