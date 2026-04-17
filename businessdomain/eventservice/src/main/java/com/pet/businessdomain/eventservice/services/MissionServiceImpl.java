package com.pet.businessdomain.eventservice.services;

import com.pet.businessdomain.eventservice.entities.*;
import com.pet.businessdomain.eventservice.exceptions.BusinessValidationException;
import com.pet.businessdomain.eventservice.exceptions.ResourceNotFoundException;
import com.pet.businessdomain.eventservice.mapper.CharacterMissionRecordMapper;
import com.pet.businessdomain.eventservice.mapper.MissionChainMapper;
import com.pet.businessdomain.eventservice.mapper.MissionMapper;
import com.pet.businessdomain.eventservice.repository.*;
import com.pet.businessdomain.eventservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MissionChainRepository chainRepository;
    private final CharacterMissionRecordRepository recordRepository;
    private final ObjectiveRepository objectiveRepository;
    private final BusinessTransactions businessTransactions;

    private final MissionMapper missionMapper;
    private final MissionChainMapper chainMapper;
    private final CharacterMissionRecordMapper recordMapper;

    private static final int MAX_ACTIVE_MISSIONS = 10;

    @Override
    public MissionResponseDto createMission(MissionResponseDto missionDto) {
        log.info("Creating mission with code: {}", missionDto.getCode());

        if (missionRepository.existsByCode(missionDto.getCode())) {
            throw new BusinessValidationException("Mission code already exists: " + missionDto.getCode());
        }

        if (missionDto.getChainId() != null) {
            validateMissionChain(missionDto.getChainId());
        }

        MissionEntity mission = missionMapper.toEntity(missionDto);
        mission.setCreatedAt(LocalDateTime.now());

        MissionEntity savedMission = missionRepository.save(mission);
        log.info("Mission created successfully with ID: {}", savedMission.getId());

        return missionMapper.toDto(savedMission);
    }

    @Override
    public MissionResponseDto updateMission(Long id, MissionResponseDto missionDto) {
        log.info("Updating mission with ID: {}", id);

        MissionEntity mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found with ID: " + id));

        if (missionDto.getChainId() != null && !missionDto.getChainId().equals(mission.getChainId())) {
            validateMissionChain(missionDto.getChainId());
        }

        MissionEntity updatedEntity = missionMapper.toEntity(missionDto);
        updatedEntity.setId(id);
        updatedEntity.setCreatedAt(mission.getCreatedAt());

        MissionEntity savedMission = missionRepository.save(updatedEntity);
        log.info("Mission updated successfully with ID: {}", id);

        return missionMapper.toDto(savedMission);
    }

    @Override
    @Transactional(readOnly = true)
    public MissionResponseDto getMissionById(Long id) {
        log.debug("Fetching mission with ID: {}", id);

        MissionEntity mission = missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found with ID: " + id));

        return missionMapper.toDto(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public MissionResponseDto getMissionByCode(String code) {
        log.debug("Fetching mission with code: {}", code);

        MissionEntity mission = missionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found with code: " + code));

        return missionMapper.toDto(mission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getAllMissions() {
        log.debug("Fetching all missions");

        return missionRepository.findAll().stream()
                .map(missionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MissionResponseDto> getAllMissionsPaginated(Pageable pageable) {
        log.debug("Fetching all missions with pagination");

        return missionRepository.findAll(pageable)
                .map(missionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getMissionsByCategory(String category) {
        log.debug("Fetching missions by category: {}", category);

        return missionRepository.findByCategory(EnumAll.MissionCategory.valueOf(category)).stream()
                .map(missionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getMissionsByType(String type) {
        log.debug("Fetching missions by type: {}", type);

        return missionRepository.findByType(EnumAll.MissionType.valueOf(type)).stream()
                .map(missionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMission(Long id) {
        log.info("Deleting mission with ID: {}", id);

        if (!missionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mission not found with ID: " + id);
        }

        missionRepository.deleteById(id);
        log.info("Mission deleted successfully with ID: {}", id);
    }

    @Override
    public MissionChainResponseDto createMissionChain(MissionChainResponseDto chainDto) {
        log.info("Creating mission chain with code: {}", chainDto.getCode());

        if (chainRepository.existsByCode(chainDto.getCode())) {
            throw new BusinessValidationException("Mission chain code already exists: " + chainDto.getCode());
        }

        MissionChainEntity chain = chainMapper.toEntity(chainDto);
        chain.setCreatedAt(LocalDateTime.now());

        MissionChainEntity savedChain = chainRepository.save(chain);
        log.info("Mission chain created successfully with ID: {}", savedChain.getId());

        return chainMapper.toDto(savedChain);
    }

    @Override
    public MissionChainResponseDto updateMissionChain(Long id, MissionChainResponseDto chainDto) {
        log.info("Updating mission chain with ID: {}", id);

        MissionChainEntity chain = chainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission chain not found with ID: " + id));

        MissionChainEntity updatedEntity = chainMapper.toEntity(chainDto);
        updatedEntity.setId(id);
        updatedEntity.setCreatedAt(chain.getCreatedAt());

        MissionChainEntity savedChain = chainRepository.save(updatedEntity);
        log.info("Mission chain updated successfully with ID: {}", id);

        return chainMapper.toDto(savedChain);
    }

    @Override
    @Transactional(readOnly = true)
    public MissionChainResponseDto getMissionChainById(Long id) {
        log.debug("Fetching mission chain with ID: {}", id);

        MissionChainEntity chain = chainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission chain not found with ID: " + id));

        return chainMapper.toDto(chain);
    }

    @Override
    @Transactional(readOnly = true)
    public MissionChainResponseDto getMissionChainByCode(String code) {
        log.debug("Fetching mission chain with code: {}", code);

        MissionChainEntity chain = chainRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Mission chain not found with code: " + code));

        return chainMapper.toDto(chain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionChainResponseDto> getAllMissionChains() {
        log.debug("Fetching all mission chains");

        return chainRepository.findAll().stream()
                .map(chainMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMissionChain(Long id) {
        log.info("Deleting mission chain with ID: {}", id);

        if (!chainRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mission chain not found with ID: " + id);
        }

        chainRepository.deleteById(id);
        log.info("Mission chain deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getAvailableMissionsForCharacter(Long characterId) {
        log.debug("Fetching available missions for character {}", characterId);

        CharacterDto character = businessTransactions.getPerson(characterId);

        if (character == null) {
            throw new ResourceNotFoundException("Character not found with ID: " + characterId);
        }

        return missionRepository.findByHiddenFalse().stream()
                .filter(mission -> isMissionAvailableForCharacter(mission, character))
                .map(missionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterMissionRecordResponseDto acceptMission(AcceptMissionRequestDto request) {
        log.info("Character {} accepting mission {}", request.getCharacterId(), request.getMissionId());

        CharacterDto character = businessTransactions.getPerson(request.getCharacterId());
        if (character == null) {
            throw new ResourceNotFoundException("Character not found with ID: " + request.getCharacterId());
        }

        MissionEntity mission = missionRepository.findById(request.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found with ID: " + request.getMissionId()));

        validateMissionAcceptance(character, mission);

        CharacterMissionRecord record = new CharacterMissionRecord();
        record.setCharacterId(request.getCharacterId());
        record.setMissionId(request.getMissionId());
        record.setStatus(EnumAll.MissionStatus.ACTIVE);
        record.setAcceptedAt(LocalDateTime.now());
        record.setTimesCompleted(0);
        record.setRewardsClaimed(false);
        record.setUpdatedAt(LocalDateTime.now());

        initializeObjectiveProgress(record, mission);

        CharacterMissionRecord savedRecord = recordRepository.save(record);
        log.info("Mission {} accepted successfully by character {}", request.getMissionId(), request.getCharacterId());

        return recordMapper.toDto(savedRecord);
    }

    @Override
    public CharacterMissionRecordResponseDto updateObjectiveProgress(UpdateObjectiveProgressRequestDto request) {
        log.info("Updating objective progress for mission {}", request.getMissionId());

        CharacterMissionRecord record = recordRepository
                .findByCharacterIdAndMissionIdAndStatus(request.getCharacterId(), request.getMissionId(),
                        EnumAll.MissionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Active mission record not found"));

        Map<Long, Integer> progress = record.getObjectiveProgress();
        if (progress == null) {
            progress = new HashMap<>();
        }

        progress.put(request.getObjectiveId(), request.getValue());
        record.setObjectiveProgress(progress);
        record.setUpdatedAt(LocalDateTime.now());

        if (areAllObjectivesCompleted(record)) {
            record.setStatus(EnumAll.MissionStatus.COMPLETED);
            record.setCompletedAt(LocalDateTime.now());
        }

        CharacterMissionRecord updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    @Override
    public CharacterMissionRecordResponseDto completeMission(Long recordId) {
        log.info("Completing mission record {}", recordId);

        CharacterMissionRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission record not found with ID: " + recordId));

        if (record.getStatus() != EnumAll.MissionStatus.ACTIVE) {
            throw new BusinessValidationException("Mission is not in progress");
        }

        if (!areAllObjectivesCompleted(record)) {
            throw new BusinessValidationException("Not all objectives are completed");
        }

        record.setStatus(EnumAll.MissionStatus.COMPLETED);
        record.setCompletedAt(LocalDateTime.now());
        record.setTimesCompleted(record.getTimesCompleted() + 1);
        record.setUpdatedAt(LocalDateTime.now());

        CharacterMissionRecord updatedRecord = recordRepository.save(record);

        processMissionCompletion(record.getCharacterId(), record.getMissionId());

        return recordMapper.toDto(updatedRecord);
    }

    @Override
    public CharacterMissionRecordResponseDto failMission(Long recordId) {
        log.info("Failing mission record {}", recordId);

        CharacterMissionRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission record not found with ID: " + recordId));

        record.setStatus(EnumAll.MissionStatus.FAILED);
        record.setFailedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        CharacterMissionRecord updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    @Override
    public CharacterMissionRecordResponseDto abandonMission(Long recordId) {
        log.info("Abandoning mission record {}", recordId);

        CharacterMissionRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission record not found with ID: " + recordId));

        record.setStatus(EnumAll.MissionStatus.ABANDONED);
        record.setUpdatedAt(LocalDateTime.now());

        CharacterMissionRecord updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    @Override
    public CharacterMissionRecordResponseDto claimMissionRewards(Long recordId) {
        log.info("Claiming rewards for mission record {}", recordId);

        CharacterMissionRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission record not found with ID: " + recordId));

        if (record.isRewardsClaimed()) {
            throw new BusinessValidationException("Rewards already claimed for this mission");
        }

        if (record.getStatus() != EnumAll.MissionStatus.COMPLETED) {
            throw new BusinessValidationException("Mission must be completed before claiming rewards");
        }

        MissionEntity mission = missionRepository.findById(record.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));

        processMissionRewards(record.getCharacterId(), mission);

        record.setRewardsClaimed(true);
        CharacterMissionRecord updatedRecord = recordRepository.save(record);

        return recordMapper.toDto(updatedRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterMissionRecordResponseDto> getCharacterActiveMissions(Long characterId) {
        log.debug("Fetching active missions for character {}", characterId);

        return recordRepository.findByCharacterIdAndStatus(characterId, EnumAll.MissionStatus.ACTIVE).stream()
                .map(recordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterMissionRecordResponseDto> getCharacterCompletedMissions(Long characterId) {
        log.debug("Fetching completed missions for character {}", characterId);

        return recordRepository.findByCharacterIdAndStatus(characterId, EnumAll.MissionStatus.COMPLETED).stream()
                .map(recordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CharacterMissionRecordResponseDto> getCharacterMissionHistory(Long characterId, Pageable pageable) {
        log.debug("Fetching mission history for character {}", characterId);

        return recordRepository.findByCharacterId(characterId, pageable)
                .map(recordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterMissionRecordResponseDto getCharacterMissionRecord(Long characterId, Long missionId) {
        log.debug("Fetching mission record for character {} and mission {}", characterId, missionId);

        CharacterMissionRecord record = recordRepository
                .findByCharacterIdAndMissionId(characterId, missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission record not found"));

        return recordMapper.toDto(record);
    }

    @Override
    public void checkAndAutoAcceptMissions(Long characterId) {
        log.info("Checking auto-accept missions for character {}", characterId);

        CharacterDto character = businessTransactions.getPerson(characterId);
        if (character == null) {
            throw new ResourceNotFoundException("Character not found with ID: " + characterId);
        }

        List<MissionEntity> autoAcceptMissions = missionRepository.findByAutoAcceptTrue();

        for (MissionEntity mission : autoAcceptMissions) {
            if (isMissionAvailableForCharacter(mission, character)) {
                try {
                    AcceptMissionRequestDto request = new AcceptMissionRequestDto();
                    request.setCharacterId(characterId);
                    request.setMissionId(mission.getId());
                    acceptMission(request);
                } catch (Exception e) {
                    log.error("Failed to auto-accept mission {} for character {}: {}",
                            mission.getId(), characterId, e.getMessage());
                }
            }
        }
    }

    @Override
    public void checkExpiredMissions(Long characterId) {
        log.info("Checking expired missions for character {}", characterId);

        List<CharacterMissionRecord> activeMissions = recordRepository
                .findByCharacterIdAndStatus(characterId, EnumAll.MissionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        for (CharacterMissionRecord record : activeMissions) {
            if (record.getExpiresAt() != null && record.getExpiresAt().isBefore(now)) {
                record.setStatus(EnumAll.MissionStatus.FAILED);
                record.setFailedAt(now);
                record.setUpdatedAt(now);
                recordRepository.save(record);
                log.info("Mission {} expired for character {}", record.getMissionId(), characterId);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponseDto> getMissionsUnlockedByCompletion(String missionCode) {
        log.debug("Fetching missions unlocked by completion of {}", missionCode);

        return missionRepository.findByUnlocksMissionCodesContaining(missionCode).stream()
                .map(missionMapper::toDto)
                .collect(Collectors.toList());
    }

    // Métodos privados de utilidad
    private void validateMissionChain(Long chainId) {
        if (!chainRepository.existsById(chainId)) {
            throw new ResourceNotFoundException("Mission chain not found with ID: " + chainId);
        }
    }

    private boolean isMissionAvailableForCharacter(MissionEntity mission, CharacterDto character) {
        if (mission.getMinAge() != null && character.getAge() < mission.getMinAge()) {
            return false;
        }

        if (mission.getRequiredXpJobs() != null && character.getXpJobs() < mission.getRequiredXpJobs()) {
            return false;
        }

        if (mission.getRequiredXpAcademy() != null && character.getXpAcademy() < mission.getRequiredXpAcademy()) {
            return false;
        }

        return true;
    }

    private void validateMissionAcceptance(CharacterDto character, MissionEntity mission) {
        if (!isMissionAvailableForCharacter(mission, character)) {
            throw new BusinessValidationException("Character does not meet mission requirements");
        }

        long activeMissionsCount = recordRepository
                .countByCharacterIdAndStatus(character.getId(), EnumAll.MissionStatus.ACTIVE);

        if (activeMissionsCount >= MAX_ACTIVE_MISSIONS) {
            throw new BusinessValidationException("Character has reached maximum active missions");
        }

        if (recordRepository.existsByCharacterIdAndMissionIdAndStatus(
                character.getId(), mission.getId(), EnumAll.MissionStatus.ACTIVE)) {
            throw new BusinessValidationException("Mission is already in progress");
        }
    }

    private void initializeObjectiveProgress(CharacterMissionRecord record, MissionEntity mission) {
        Map<Long, Integer> progress = new HashMap<>();

        if (mission.getObjectives() != null) {
            for (ObjectiveEntity objective : mission.getObjectives()) {
                progress.put(objective.getId(), objective.getCurrentDefault() != null ?
                        objective.getCurrentDefault() : 0);
            }
        }

        record.setObjectiveProgress(progress);
    }

    private boolean areAllObjectivesCompleted(CharacterMissionRecord record) {
        MissionEntity mission = missionRepository.findById(record.getMissionId())
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));

        Map<Long, Integer> progress = record.getObjectiveProgress();

        if (mission.getObjectives() == null || mission.getObjectives().isEmpty()) {
            return true;
        }

        for (ObjectiveEntity objective : mission.getObjectives()) {
            if (!objective.isOptional()) {
                Integer currentValue = progress.get(objective.getId());
                if (currentValue == null || currentValue < objective.getTargetValue()) {
                    return false;
                }
            }
        }

        return true;
    }

    private void processMissionCompletion(Long characterId, Long missionId) {
        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found"));

//        characterServiceClient.addCompletedMission(characterId, mission.getCode());
    }
    @Override
    @Transactional
    public List<MissionResponseDto> createMissionBatch(List<MissionResponseDto> dtos) {
        log.info("Processing batch of {} missions", dtos.size());

        List<MissionEntity> entitiesToSave = new ArrayList<>();
        List<MissionResponseDto> result = new ArrayList<>();

        for (MissionResponseDto dto : dtos) {
            // Si ya existe, lo saltamos
            if (missionRepository.existsByCode(dto.getCode())) {
                log.warn("Mission {} already exists, skipping", dto.getCode());
                continue;
            }

            MissionEntity mission = missionMapper.toEntity(dto);
            mission.setCreatedAt(LocalDateTime.now());
            entitiesToSave.add(mission);
        }

        if (!entitiesToSave.isEmpty()) {
            List<MissionEntity> saved = missionRepository.saveAll(entitiesToSave);
            result = saved.stream()
                    .map(missionMapper::toDto)
                    .collect(Collectors.toList());
        }

        log.info("Successfully created {} missions", result.size());
        return result;
    }
    private void processMissionRewards(Long characterId, MissionEntity mission) {
//        if (mission.getRewards() != null) {
//            characterServiceClient.applyRewards(characterId, mission.getRewards());
//        }
    }
}