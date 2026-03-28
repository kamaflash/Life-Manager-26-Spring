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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterServiceImpl implements CharacterService {

    @Autowired
    private final CharacterRepository characterRepository;
    @Autowired
    private final CharacterMapper characterMapper;

    @Autowired
    private final BusinessTransactions businessTransactions;

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
}

