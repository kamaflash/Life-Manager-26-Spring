package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.jobservice.entities.RequirementEntity;
import com.pet.businessdomain.jobservice.mapper.RequirementMapper;
import com.pet.businessdomain.jobservice.repository.JobVacancyRepository;
import com.pet.businessdomain.jobservice.repository.RequirementRepository;
import com.pet.businessdomain.jobservice.services.RequirementService;
import com.pet.businessdomain.shareddto.dto.RequirementDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;
    private final JobVacancyRepository jobVacancyRepository;
    private final RequirementMapper requirementMapper;

    @Override
    @Transactional
    public RequirementDTO create(RequirementDTO dto) {
        log.info("Creating new requirement for vacancy: {}", dto.getVacancyId());

        RequirementEntity entity = requirementMapper.toEntity(dto);

        // Asignar relación con JobVacancy
        if (dto.getVacancyId() != null) {
            JobVacancyEntity vacancy = jobVacancyRepository.findById(dto.getVacancyId())
                    .orElseThrow(() -> new RuntimeException("Job vacancy not found with id: " + dto.getVacancyId()));
            entity.setVacancy(vacancy);
        }

        RequirementEntity saved = requirementRepository.save(entity);
        return requirementMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RequirementDTO update(Long id, RequirementDTO dto) {
        log.info("Updating requirement with id: {}", id);

        RequirementEntity entity = requirementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requirement not found with id: " + id));

        requirementMapper.updateEntity(dto, entity);
        RequirementEntity updated = requirementRepository.save(entity);
        return requirementMapper.toDto(updated);
    }

    @Override
    public RequirementDTO getById(Long id) {
        return requirementRepository.findById(id)
                .map(requirementMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Requirement not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting requirement with id: {}", id);
        requirementRepository.deleteById(id);
    }

    @Override
    public List<RequirementDTO> getByVacancyId(Long vacancyId) {
        return requirementMapper.toDtoList(requirementRepository.findByVacancyId(vacancyId));
    }

    @Override
    @Transactional
    public void deleteByVacancyId(Long vacancyId) {
        log.info("Deleting all requirements for vacancy: {}", vacancyId);
        requirementRepository.deleteByVacancyId(vacancyId);
    }

    @Override
    @Transactional
    public List<RequirementDTO> createBatch(List<RequirementDTO> dtos) {
        log.info("Creating batch of {} requirements", dtos.size());

        List<RequirementDTO> created = new ArrayList<>();
        for (RequirementDTO dto : dtos) {
            RequirementDTO createdDto = create(dto);
            created.add(createdDto);
        }

        log.info("Successfully created {} requirements", created.size());
        return created;
    }
}