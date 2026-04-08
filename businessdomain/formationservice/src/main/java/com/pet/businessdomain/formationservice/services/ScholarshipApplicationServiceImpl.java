package com.pet.businessdomain.formationservice.services;

import com.pet.businessdomain.formationservice.entities.CharacterTraining;
import com.pet.businessdomain.formationservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.NotificationDTO;
import com.pet.businessdomain.shareddto.dto.ScholarshipApplicationDto;
import com.pet.businessdomain.formationservice.entities.ScholarshipApplicationEntity;
import com.pet.businessdomain.formationservice.entities.ScholarshipEntity;
import com.pet.businessdomain.formationservice.mapper.ScholarshipApplicationMapper;
import com.pet.businessdomain.formationservice.mapper.ScholarshipMapper;
import com.pet.businessdomain.formationservice.repository.ScholarshipApplicationRepository;
import com.pet.businessdomain.formationservice.repository.ScholarshipRepository;
import java.time.LocalDate;

import com.pet.businessdomain.shareddto.dto.SystemDto;
import com.pet.businessdomain.shareddto.enumentities.EnumFormation;
import com.pet.businessdomain.shareddto.enumentities.NotificationResourceType;
import com.pet.businessdomain.shareddto.enumentities.NotificationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private BusinessTransactions businessTransactions;

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
                .appliedAt(LocalDate.now())
                .status(EnumFormation.ApplicationStatus.PENDING)
                .build();
        createNotification(scholarship, application);
        SystemDto systemDto = businessTransactions.getSystem(application.getCharacterId());
        systemDto.setPa(systemDto.getPa() - 1);
        LocalDateTime current = systemDto.getActualityAt();

        // Sumamos 1 día y ajustamos la hora y minuto según LocalTime
        LocalDateTime newActuality = current.plusHours(1);
        systemDto.setActualityAt(newActuality);
        systemDto = businessTransactions.updateSystem(systemDto.getUid(),newActuality,1);
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
    public ScholarshipApplicationDto updateStatus(Long applicationId, EnumFormation.ApplicationStatus statuss) {
        ScholarshipApplicationEntity application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus( statuss );

        return mapperA.toDto(applicationRepository.save(application));
    }
    @Override
    public boolean hasApplied(Long scholarshipId, Long characterId) {
        return applicationRepository
                .findByScholarshipIdAndCharacterId(scholarshipId, characterId)
                .isPresent();  // Si existe, ya aplicó
    }

    private void createNotification( ScholarshipEntity scholarship, ScholarshipApplicationEntity application ) {
        NotificationDTO notificationDTODto = new NotificationDTO();
        notificationDTODto.setTitle("Has aplicado a una nueva beca");
        notificationDTODto.setSubTitle("Has aplicado a la beca "+scholarship.getTitle());
        notificationDTODto.setMessage("Has aplicado a la beca "+scholarship.getTitle());
        notificationDTODto.setFromUserId(application.getCharacterId());
        notificationDTODto.setActionUrl("/");
        notificationDTODto.setType(NotificationType.NEW_CONTENT);
        notificationDTODto.setUserId(application.getCharacterId());
        notificationDTODto.setRead(false);
        notificationDTODto.setResourceId(application.getId());
        notificationDTODto.setResourceType(NotificationResourceType.COURSE);

        businessTransactions.setNotifications(notificationDTODto);
    }
}
