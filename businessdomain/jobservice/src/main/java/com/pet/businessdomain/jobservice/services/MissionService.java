package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.shareddto.dto.MissionDTO;

import java.util.List;

public interface MissionService {

    // ===== CRUD BÁSICO =====
    MissionDTO create(MissionDTO dto);
    MissionDTO getById(Long id);
    void delete(Long id);

    // ===== LISTADOS =====
    List<MissionDTO> getByCharacterJob(Long characterJobId);
    List<MissionDTO> getActiveMissions(Long characterJobId);
    List<MissionDTO> getCompletedMissions(Long characterJobId);
    List<MissionDTO> getMissionsByDifficulty(String difficulty);
    List<MissionDTO> getExpiredMissions();
    List<MissionDTO> getExpiringSoon();

    // ===== ACCIONES =====
    MissionDTO assignMission(Long characterJobId, MissionDTO mission);
    MissionDTO completeMission(Long missionId);
    void failMission(Long missionId);
    void checkExpiredMissions();

    // ===== RECOMPENSAS =====
    void applyRewards(Long characterJobId, MissionDTO mission);

    // ===== ESTADÍSTICAS =====
    Double getCompletionRate(Long characterJobId);
    MissionDTO getRandomMissionByDifficulty(Long characterJobId, String difficulty);

    // ===== VERIFICACIONES =====
    boolean isMissionExpired(Long missionId);
    boolean canCompleteMission(Long missionId);
}