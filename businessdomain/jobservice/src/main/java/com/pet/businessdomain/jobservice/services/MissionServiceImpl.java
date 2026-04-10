package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.MissionEntity;
import com.pet.businessdomain.jobservice.mapper.MissionMapper;
import com.pet.businessdomain.jobservice.repository.MissionRepository;
import com.pet.businessdomain.jobservice.services.MissionService;
import com.pet.businessdomain.shareddto.dto.MissionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final MissionMapper missionMapper;

    @Override
    @Transactional
    public MissionDTO create(MissionDTO dto) {
        log.info("Creating new mission: {}", dto.getTitle());

        MissionEntity entity = missionMapper.toEntity(dto);
        MissionEntity saved = missionRepository.save(entity);
        return missionMapper.toDto(saved);
    }

    @Override
    public MissionDTO getById(Long id) {
        return missionRepository.findById(id)
                .map(missionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting mission with id: {}", id);
        missionRepository.deleteById(id);
    }

    @Override
    public List<MissionDTO> getByCharacterJob(Long characterJobId) {
        return missionMapper.toDtoList(missionRepository.findByCharacterJobId(characterJobId));
    }

    @Override
    public List<MissionDTO> getActiveMissions(Long characterJobId) {
        // ✅ Usar el método simplificado del repositorio
        List<MissionEntity> missions = missionRepository.findByCharacterJobIdAndCompletedFalse(characterJobId);

        // Filtrar las que no han expirado
        return missions.stream()
                .filter(mission -> !isMissionExpired(mission))
                .map(missionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MissionDTO> getCompletedMissions(Long characterJobId) {
        return missionMapper.toDtoList(missionRepository.findByCharacterJobIdAndCompletedTrue(characterJobId));
    }

    @Override
    public List<MissionDTO> getMissionsByDifficulty(String difficulty) {
        return missionMapper.toDtoList(missionRepository.findByDifficulty(difficulty));
    }

    @Override
    public List<MissionDTO> getExpiredMissions() {
        // Calcular tiempo de expiración (hace 1 hora por defecto, o ajusta según necesidad)
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(1);
        return missionMapper.toDtoList(missionRepository.findExpiredMissions(expirationTime));
    }

    @Override
    public List<MissionDTO> getExpiringSoon() {
        // Misiones que expiran en las próximas 24 horas
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(24);
        return missionMapper.toDtoList(missionRepository.findMissionsExpiringSoon(startTime, endTime));
    }

    @Override
    @Transactional
    public MissionDTO assignMission(Long characterJobId, MissionDTO mission) {
        log.info("Assigning mission to job {}", characterJobId);

        mission.setCharacterJobId(characterJobId);
        mission.setAssignedAt(LocalDateTime.now());
        mission.setCompleted(false);

        return create(mission);
    }

    @Override
    @Transactional
    public MissionDTO completeMission(Long missionId) {
        log.info("Completing mission {}", missionId);

        MissionEntity mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + missionId));

        if (isMissionExpired(mission)) {
            throw new RuntimeException("Cannot complete expired mission");
        }

        mission.setCompleted(true);
        mission.setCompletedAt(LocalDateTime.now());

        MissionEntity saved = missionRepository.save(mission);

        // Aplicar recompensas
        applyRewards(mission.getCharacterJobId(), missionMapper.toDto(saved));

        return missionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void failMission(Long missionId) {
        log.info("Failing mission {}", missionId);
        MissionEntity mission = missionRepository.findById(missionId)
                .orElse(null);
        if (mission != null) {
            missionRepository.delete(mission);
        }
    }

    @Override
    @Transactional
    public void checkExpiredMissions() {
        log.info("Checking for expired missions");
        // Calcular tiempo de expiración
        LocalDateTime expirationTime = LocalDateTime.now();
        List<MissionEntity> expired = missionRepository.findExpiredMissions(expirationTime);
        for (MissionEntity mission : expired) {
            failMission(mission.getId());
        }
    }

    @Override
    @Transactional
    public void applyRewards(Long characterJobId, MissionDTO mission) {
        log.info("Applying rewards for mission {}: XP={}, Bonus={}, Reputation={}",
                mission.getId(), mission.getXpReward(), mission.getSalaryBonus(), mission.getReputationGain());

        // TODO: Implementar lógica para aplicar recompensas al personaje
        // - Añadir XP
        // - Añadir bonus al salario
        // - Aumentar reputación
        // - Mejorar habilidades
    }

    @Override
    public Double getCompletionRate(Long characterJobId) {
        Double rate = missionRepository.calculateCompletionRate(characterJobId);
        return rate != null ? rate : 0.0;
    }

    @Override
    public MissionDTO getRandomMissionByDifficulty(Long characterJobId, String difficulty) {
        List<MissionDTO> missions = getMissionsByDifficulty(difficulty);
        if (missions.isEmpty()) return null;

        int randomIndex = (int) (Math.random() * missions.size());
        MissionDTO mission = missions.get(randomIndex);
        mission.setCharacterJobId(characterJobId);

        return mission;
    }

    @Override
    public boolean isMissionExpired(Long missionId) {
        return missionRepository.findById(missionId)
                .map(this::isMissionExpired)
                .orElse(true);
    }

    // Método auxiliar para verificar si una misión está expirada
    private boolean isMissionExpired(MissionEntity mission) {
        if (mission.getCompleted()) return false;
        if (mission.getDeadlineHours() == null) return false;

        LocalDateTime deadline = mission.getAssignedAt()
                .plusHours(mission.getDeadlineHours());
        return LocalDateTime.now().isAfter(deadline);
    }

    @Override
    public boolean canCompleteMission(Long missionId) {
        return !isMissionExpired(missionId);
    }
}