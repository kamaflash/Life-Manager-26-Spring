package com.pet.businessdomain.eventservice.services;

import com.pet.businessdomain.shareddto.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MissionService {

    // Gestión de Misiones
    MissionResponseDto createMission(MissionResponseDto missionDto);
    MissionResponseDto updateMission(Long id, MissionResponseDto missionDto);
    MissionResponseDto getMissionById(Long id);
    MissionResponseDto getMissionByCode(String code);
    List<MissionResponseDto> getAllMissions();
    Page<MissionResponseDto> getAllMissionsPaginated(Pageable pageable);
    List<MissionResponseDto> getMissionsByCategory(String category);
    List<MissionResponseDto> getMissionsByType(String type);
    void deleteMission(Long id);

    // Gestión de Cadenas de Misiones
    MissionChainResponseDto createMissionChain(MissionChainResponseDto chainDto);
    MissionChainResponseDto updateMissionChain(Long id, MissionChainResponseDto chainDto);
    MissionChainResponseDto getMissionChainById(Long id);
    MissionChainResponseDto getMissionChainByCode(String code);
    List<MissionChainResponseDto> getAllMissionChains();
    void deleteMissionChain(Long id);

    // Gestión de Misiones para Personajes
    List<MissionResponseDto> getAvailableMissionsForCharacter(Long characterId);
    CharacterMissionRecordResponseDto acceptMission(AcceptMissionRequestDto request);
    CharacterMissionRecordResponseDto updateObjectiveProgress(UpdateObjectiveProgressRequestDto request);
    CharacterMissionRecordResponseDto completeMission(Long recordId);
    CharacterMissionRecordResponseDto failMission(Long recordId);
    CharacterMissionRecordResponseDto abandonMission(Long recordId);
    CharacterMissionRecordResponseDto claimMissionRewards(Long recordId);

    // Consultas de Misiones de Personaje
    List<CharacterMissionRecordResponseDto> getCharacterActiveMissions(Long characterId);
    List<CharacterMissionRecordResponseDto> getCharacterCompletedMissions(Long characterId);
    Page<CharacterMissionRecordResponseDto> getCharacterMissionHistory(Long characterId, Pageable pageable);
    CharacterMissionRecordResponseDto getCharacterMissionRecord(Long characterId, Long missionId);

    // Procesamiento automático
    void checkAndAutoAcceptMissions(Long characterId);
    void checkExpiredMissions(Long characterId);
    List<MissionResponseDto> getMissionsUnlockedByCompletion(String missionCode);
    List<MissionResponseDto> createMissionBatch(List<MissionResponseDto> dtos);

}