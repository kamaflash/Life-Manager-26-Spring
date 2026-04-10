package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.WorkRelationshipDTO;

import java.util.List;

public interface WorkRelationshipService {

    // ===== CRUD BÁSICO =====
    WorkRelationshipDTO create(WorkRelationshipDTO dto);
    WorkRelationshipDTO update(Long id, WorkRelationshipDTO dto);
    WorkRelationshipDTO getById(Long id);
    void delete(Long id);

    // ===== LISTADOS =====
    List<WorkRelationshipDTO> getByCharacterJob(Long characterJobId);
    List<WorkRelationshipDTO> getBoss(Long characterJobId);
    List<WorkRelationshipDTO> getMentor(Long characterJobId);
    List<WorkRelationshipDTO> getColleagues(Long characterJobId);
    List<WorkRelationshipDTO> getRivals(Long characterJobId);
    List<WorkRelationshipDTO> getHighAffinity(Long characterJobId);
    List<WorkRelationshipDTO> getLowAffinity(Long characterJobId);

    // ===== INTERACCIONES =====
    WorkRelationshipDTO improveAffinity(Long id, Integer amount);
    WorkRelationshipDTO worsenAffinity(Long id, Integer amount);

    // ===== VERIFICACIONES =====
    boolean hasBoss(Long characterJobId);
    boolean hasMentor(Long characterJobId);
    boolean existsRelationship(Long characterJobId, Long relatedCharacterId);

    // ===== INICIALIZACIÓN =====
    void initializeRelationships(Long characterJobId, Long companyId);
}