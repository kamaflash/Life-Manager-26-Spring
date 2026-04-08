package com.pet.businessdomain.personservice.services;

import com.pet.businessdomain.personservice.entities.CharacterEntity;
import com.pet.businessdomain.personservice.entities.CharacterStats;
import com.pet.businessdomain.personservice.entities.SkillStateEmbeddable;
import com.pet.businessdomain.personservice.mapper.CharacterMapper;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.personservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.NotificationResourceType;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CharacterServiceImpl implements CharacterService {

    private static final int BASE_XP_PER_LEVEL = 100;
    private static final int MAX_SKILL_LEVEL = 10;
    private static final int MIN_XP_PER_SKILL = 10;
    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterMapper characterMapper;

    @Autowired
    private BusinessTransactions businessTransactions;

    @Override
    public CharacterDto createCharacter(CharacterDto characterDto) {
        CharacterEntity entity = characterMapper.toEntity(characterDto);
        entity.setCreatedAt(LocalDateTime.now());
        CharacterEntity saved = characterRepository.save(entity);
        CharacterDto dto = characterMapper.toDto(saved);
        SFinanceAccountResponseDto sfinanceAccountResponseDto = businessTransactions.setAccount(createAccountMock(dto.getId(), characterDto),characterDto.getNewIncome(),characterDto.getNewExpense());
        List<SFinanceAccountResponseDto> listAccount = new ArrayList<>();
        listAccount.add(sfinanceAccountResponseDto);
        saved.setAccounts(listAccount);
        CharacterTrainingDto trainerDto = businessTransactions.setEducation(characterDto.getEducation().getFirst(),saved.getId());
        SystemDto systemDto = new SystemDto();
        systemDto.setUid(saved.getUid());
        systemDto.setVeces(0);
        SystemDto systemDtoSave = businessTransactions.setSystem(systemDto);
        dto.setId(saved.getId());
        setNotification(dto);
        return characterMapper.toDto(saved);
    }

    @Override
    public CharacterDto getCharacterById(Long id) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));
        entity.setAccounts(businessTransactions.getAccount(id));
        CharacterDto dto = characterMapper.toDto(entity);
        dto.setEducation(businessTransactions.getEducation(entity.getId()));
        CharacterApplicationDto aDto = businessTransactions.getJobsApplication(entity.getId());
        if(aDto.getCharacterId() != null) {
            dto.setJobs(businessTransactions.getJobs(aDto.getVacancyId()));
        }
        dto.setInventory(businessTransactions.getInventory(id));

        return dto;
    }

    @Override
    public CharacterDto getCharacterByUid(Long uid) {
        CharacterEntity entity = characterRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + uid));
        entity.setAccounts(businessTransactions.getAccount(entity.getId()));
        CharacterDto dto = characterMapper.toDto(entity);
        dto.setEducation(businessTransactions.getEducation(entity.getId()));
        CharacterApplicationDto aDto = businessTransactions.getJobsApplication(entity.getId());
        if(aDto.getCharacterId() != null) {
            dto.setJobs(businessTransactions.getJobs(aDto.getVacancyId()));
        }
        dto.setInventory(businessTransactions.getInvetory(entity.getId()));
        return dto;
    }

    @Override
    public List<CharacterDto> getAllCharacters() {
        return characterRepository.findAll()
                .stream()
                .map(characterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterDto updateCharacterName(Long id, String name) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));

        // Mapeamos los campos del DTO a la entidad existente
        entity.setName(name);
        entity.setUpdatedAt(LocalDateTime.now());

        CharacterEntity updated = characterRepository.save(entity);
        return characterMapper.toDto(updated);
    }

    @Override
    public CharacterDto updateCharacter(Long id, CharacterDto characterDto) {

        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));

        // ===== CAMPOS BÁSICOS =====
        entity.setName(characterDto.getName());
        entity.setAvatar(characterDto.getAvatar());
        entity.setAge(characterDto.getAge());
        entity.setBirthDate(characterDto.getBirthDate());
        entity.setGender(characterDto.getGender());
        entity.setCity(characterDto.getCity());
        entity.setResidentialCity(characterDto.getResidentialCity());

        entity.setXpAcademy(characterDto.getXpAcademy());
        entity.setXpJobs(characterDto.getXpJobs());

        // ===== STATS =====
        if (characterDto.getStats() != null) {

            CharacterStats stats = entity.getStats();

            stats.setIntelligence(characterDto.getStats().getIntelligence());
            stats.setCharisma(characterDto.getStats().getCharisma());
            stats.setCreativity(characterDto.getStats().getCreativity());
            stats.setResilience(characterDto.getStats().getResilience());
            stats.setHealth(characterDto.getStats().getHealth());
            stats.setEnergy(characterDto.getStats().getEnergy());
            stats.setHappiness(characterDto.getStats().getHappiness());
            stats.setStress(characterDto.getStats().getStress());
            stats.setFinances(characterDto.getStats().getFinances());
        }

        // ===== SKILLS =====
        if (characterDto.getSkills() != null) {
            entity.setSkills(
                    characterDto.getSkills().entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    e -> {
                                        SkillStateDto dto = e.getValue();
                                        SkillStateEmbeddable skill = new SkillStateEmbeddable();
                                        skill.setKeyValue(dto.getKeyValue());
                                        skill.setLevel(dto.getLevel());
                                        skill.setXp(dto.getXp());
                                        skill.setLocked(dto.getLocked());
                                        return skill;
                                    }
                            ))
            );
        }

        // ===== INTERESTS =====
        entity.setInterests(
                characterDto.getInterests() != null
                        ? new ArrayList<>(characterDto.getInterests())
                        : null
        );

        // ===== FRIENDS (String -> Long) =====
        entity.setFriends(new ArrayList<>(
                characterDto.getFriends().stream()
                        .map(Long::valueOf)
                        .toList()
        ));

        entity.setFamilyIds(new ArrayList<>(
                characterDto.getFamilyIds().stream()
                        .map(Long::valueOf)
                        .toList()
        ));

        // ===== PARTNER =====
        if (characterDto.getPartnerId() != null) {
            entity.setPartnerId(Long.valueOf(characterDto.getPartnerId()));
        }

        // ===== INVENTORY =====


        // ===== TIMESTAMP =====
        entity.setUpdatedAt(LocalDateTime.now());

        CharacterEntity updated = characterRepository.save(entity);

        return characterMapper.toDto(updated);
    }

    @Override
    public void deleteCharacter(Long id) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));
        characterRepository.delete(entity);
    }

    private SFinanceAccountResponseDto createAccountMock(Long id, CharacterDto characterDto) {
        SFinanceAccountResponseDto sfinanceAccountResponseDto = new SFinanceAccountResponseDto();
        sfinanceAccountResponseDto.setOwnerId(id);
        sfinanceAccountResponseDto.setOwnerType(EnumAll.OwnerType.CHARACTER);

        sfinanceAccountResponseDto.setBalance(
                characterDto.getNewIncome()
                        .subtract(characterDto.getNewExpense())
                        .subtract(BigDecimal.valueOf(200))
        );
        return sfinanceAccountResponseDto;
    }

    private void setNotification(CharacterDto dto) {

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(dto.getId());
        notificationDTO.setFromUserId(dto.getUid());
        notificationDTO.setType(NotificationType.SYSTEM);

        notificationDTO.setTitle("Creación de nuevo personaje");
        notificationDTO.setSubTitle("Comienza tu aventura");
        notificationDTO.setMessage("Visita tu perfil para obtener tus datos.");

        // 🔥 Nuevo sistema
        notificationDTO.setResourceType(NotificationResourceType.USER);
        notificationDTO.setResourceId(dto.getId());

        // 🔥 Navegación directa frontend
        notificationDTO.setActionUrl("/profile" );

        // 🔥 Metadata (opcional pero muy recomendable)
        notificationDTO.setMetadata("""
        {
            "characterId": %d
        }
    """.formatted(dto.getId()));

        notificationDTO.setRead(false);

        businessTransactions.setNotifications(notificationDTO);
    }

    @Override
    public CharacterSkillsUpdateResponseDto updateCharacterSkills(CharacterSkillsUpdateRequestDto request) {
        CharacterSkillsUpdateResponseDto response = new CharacterSkillsUpdateResponseDto();
        response.setCharacterId(request.getCharacterId());

        try {
            CharacterEntity character = characterRepository.findById(request.getCharacterId())
                    .orElseThrow(() -> new RuntimeException("Personaje no encontrado"));

            // 1. Actualizar STATS
            if (request.getStatRewards() != null && !request.getStatRewards().isEmpty()) {
                updateStats(character, request.getStatRewards());
            }

            // 2. Actualizar SKILLS
            Map<String, SkillStateEmbeddable> updatedSkills = updateSkills(
                    character,
                    request.getSkillsToUnlock(),
                    request.getTotalXpReward()
            );

            // 3. Convertir a DTO para respuesta
            Map<String, SkillStateDto> responseSkills = new HashMap<>();
            for (Map.Entry<String, SkillStateEmbeddable> entry : updatedSkills.entrySet()) {
                SkillStateEmbeddable s = entry.getValue();
                responseSkills.put(entry.getKey(), new SkillStateDto());
            }

            // 4. Actualizar XP académico
            int currentXp = character.getXpAcademy() != null ? character.getXpAcademy() : 0;
            character.setXpAcademy(currentXp + request.getAcademicXpReward());

            characterRepository.save(character);

            response.setUpdatedSkills(responseSkills);
            response.setNewAcademicXp(character.getXpAcademy());
            response.setUpdatedStats(mapToStatsDto(character.getStats()));
            response.setSuccess(true);
            response.setMessage("Skills actualizados correctamente");

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private void updateStats(CharacterEntity character, Map<String, Integer> rewards) {
        // Obtener o crear las estadísticas
        CharacterStats stats = character.getStats();
        if (stats == null) {
            stats = new CharacterStats();
            character.setStats(stats);
        }

        final CharacterStats finalStats = stats;

        // Mapeo de claves a setters
        rewards.forEach((key, value) -> {
            switch (key.toLowerCase()) {
                case "intelligence" -> finalStats.setIntelligence(finalStats.getIntelligence() + value);
                case "charisma" -> finalStats.setCharisma(finalStats.getCharisma() + value);
                case "creativity" -> finalStats.setCreativity(finalStats.getCreativity() + value);
                case "resilience" -> finalStats.setResilience(finalStats.getResilience() + value);
                case "finances" -> finalStats.setFinances(finalStats.getFinances() + value);
            }
        });
    }

    private Map<String, SkillStateEmbeddable> updateSkills(CharacterEntity character, List<String> skillsToUnlock, int totalXpReward) {
        if (skillsToUnlock == null || skillsToUnlock.isEmpty()) {
            return character.getSkills();
        }

        int xpPerSkill = Math.max(MIN_XP_PER_SKILL, totalXpReward / skillsToUnlock.size());

        Map<String, SkillStateEmbeddable> skills = character.getSkills();
        if (skills == null) {
            skills = new HashMap<>();
            character.setSkills(skills);
        }

        for (String skillKey : skillsToUnlock) {
            SkillStateEmbeddable skill = skills.get(skillKey);
            if (skill == null) {
                skill = new SkillStateEmbeddable();
                skill.setKeyValue(skillKey);
                skill.setLevel(1);
                skill.setXp(0);
                skill.setLocked(false);
                skills.put(skillKey, skill);
            }
            addSkillXp(skill, xpPerSkill);
        }
        return skills;
    }

    private void addSkillXp(SkillStateEmbeddable skill, int xpGained) {
        if (Boolean.TRUE.equals(skill.isLocked())) return;

        int newXp = (skill.getXp() != 0 ? skill.getXp() : 0) + xpGained;
        int newLevel = skill.getLevel() != 0 ? skill.getLevel() : 1;

        while (newXp >= BASE_XP_PER_LEVEL * newLevel && newLevel < MAX_SKILL_LEVEL) {
            newXp -= BASE_XP_PER_LEVEL * newLevel;
            newLevel++;
        }

        skill.setXp(newXp);
        skill.setLevel(newLevel);
    }

    private StatsDto mapToStatsDto(CharacterStats stats) {
        if (stats == null) return new StatsDto();
        StatsDto dto = new StatsDto();
        dto.setIntelligence(stats.getIntelligence());
        dto.setCharisma(stats.getCharisma());
        dto.setCreativity(stats.getCreativity());
        dto.setResilience(stats.getResilience());
        dto.setFinances(stats.getFinances());
        dto.setHealth(stats.getHealth());
        dto.setEnergy(stats.getEnergy());
        dto.setHappiness(stats.getHappiness());
        dto.setStress(stats.getStress());
        return dto;
    }
}

