package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.dto.ScholarshipApplicationDto;
import com.pet.businessdomain.formationservice.entities.ScholarshipApplicationEntity;
import com.pet.businessdomain.formationservice.entities.ScholarshipEntity;
import com.pet.businessdomain.formationservice.mapper.ScholarshipApplicationMapper;
import com.pet.businessdomain.formationservice.mapper.ScholarshipMapper;
import com.pet.businessdomain.formationservice.repository.ScholarshipApplicationRepository;
import com.pet.businessdomain.formationservice.repository.ScholarshipRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ScholarshipApplicationServiceImpl implements ScholarshipApplicationService {
    @Autowired
    private ScholarshipApplicationRepository applicationRepository;
    @Autowired
    private ScholarshipMapper mapper;

    @Autowired
    private ScholarshipApplicationMapper mapperA;
    @Autowired
    private  ScholarshipRepository scholarshipRepository;

    @Override
    public ScholarshipApplicationDto applyToScholarship(Long scholarshipId, Long characterId) {

        // Evitar doble aplicación
        applicationRepository.findByScholarshipIdAndCharacterId(scholarshipId, characterId)
                .ifPresent(a -> { throw new RuntimeException("Already applied"); });

        ScholarshipEntity scholarship = scholarshipRepository.findById(scholarshipId)
                .orElseThrow(() -> new RuntimeException("Scholarship not found"));

        ScholarshipApplicationEntity application = ScholarshipApplicationEntity.builder()
                .scholarshipId(scholarshipId)
                .characterId(characterId)
                .status(ScholarshipApplicationEntity.ApplicationStatus.PENDING)
                .appliedAt(LocalDate.now())
                .build();

        return mapperA.toDto(applicationRepository.save(application));
    }

    @Override
    public List<ScholarshipApplicationDto> getApplicationsByCharacter(Long characterId) {
        return mapperA.toDtoList(applicationRepository.findByCharacterId(characterId));
    }

    @Override
    public List<ScholarshipApplicationDto> getApplicationsByScholarship(Long scholarshipId) {
        return mapperA.toDtoList(applicationRepository.findByScholarshipId(scholarshipId));
    }

    @Override
    public ScholarshipApplicationDto updateStatus(Long applicationId, String status) {
        ScholarshipApplicationEntity application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(
                ScholarshipApplicationEntity.ApplicationStatus.valueOf(status.toUpperCase())
        );

        return mapperA.toDto(applicationRepository.save(application));
    }
}
