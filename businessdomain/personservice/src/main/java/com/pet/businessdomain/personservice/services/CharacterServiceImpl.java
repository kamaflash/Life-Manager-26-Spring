package com.pet.businessdomain.personservice.services;

import com.pet.businessdomain.personservice.dto.*;
import com.pet.businessdomain.personservice.entities.CharacterEntity;
import com.pet.businessdomain.personservice.entities.EducationExperienceEntity;
import com.pet.businessdomain.personservice.entities.enumentities.SEnumAccount;
import com.pet.businessdomain.personservice.mapper.CharacterMapper;
import com.pet.businessdomain.personservice.repository.CharacterRepository;
import com.pet.businessdomain.personservice.transactions.BusinessTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        return characterMapper.toDto(saved);
    }

    @Override
    public CharacterDto getCharacterById(Long id) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));
        entity.setAccounts(businessTransactions.getAccount(id));
        return characterMapper.toDto(entity);
    }

    @Override
    public CharacterDto getCharacterByUid(Long uid) {
        CharacterEntity entity = characterRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + uid));
        entity.setAccounts(businessTransactions.getAccount(entity.getId()));
        CharacterDto dto = characterMapper.toDto(entity);
        dto.setEducation(businessTransactions.getEducation(entity.getId()));
        CharacterApplicationDto aDto = businessTransactions.getJobsApplication(entity.getId());
        dto.setJobs(businessTransactions.getJobs(aDto.getVacancyId()));
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
    public CharacterDto updateCharacter(Long id, String name) {
        CharacterEntity entity = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found with id " + id));

        // Mapeamos los campos del DTO a la entidad existente
        entity.setName(name);
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
        sfinanceAccountResponseDto.setOwnerType(SEnumAccount.OwnerType.CHARACTER);

        sfinanceAccountResponseDto.setBalance(
                characterDto.getNewIncome()
                        .subtract(characterDto.getNewExpense())
                        .subtract(BigDecimal.valueOf(200))
        );
        return sfinanceAccountResponseDto;
    }
}

