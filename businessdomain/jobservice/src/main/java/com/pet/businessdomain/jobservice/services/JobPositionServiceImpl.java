package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.mapper.JobPositionMapper;
import com.pet.businessdomain.jobservice.repository.JobPositionRepository;
import com.pet.businessdomain.jobservice.services.JobPositionService;
import com.pet.businessdomain.shareddto.dto.JobPositionDTO;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPositionServiceImpl implements JobPositionService {

    private final JobPositionRepository jobPositionRepository;
    private final JobPositionMapper jobPositionMapper;

    @Override
    @Transactional
    public JobPositionDTO create(JobPositionDTO dto) {
        log.info("Creating new job position: {}", dto.getTitle());

        if (dto.getCompanyId() == null) {
            throw new RuntimeException("Company ID is required");
        }

        JobPositionEntity entity = jobPositionMapper.toEntity(dto);
        JobPositionEntity saved = jobPositionRepository.save(entity);
        return jobPositionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobPositionDTO update(Long id, JobPositionDTO dto) {
        log.info("Updating job position with id: {}", id);

        JobPositionEntity entity = jobPositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job position not found with id: " + id));

        jobPositionMapper.updateEntity(dto, entity);
        JobPositionEntity updated = jobPositionRepository.save(entity);
        return jobPositionMapper.toDto(updated);
    }

    @Override
    public JobPositionDTO getById(Long id) {
        return jobPositionRepository.findById(id)
                .map(jobPositionMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Job position not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting job position with id: {}", id);
        jobPositionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating job position with id: {}", id);
        JobPositionEntity entity = jobPositionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job position not found with id: " + id));
        entity.setActive(false);
        jobPositionRepository.save(entity);
    }

    @Override
    public List<JobPositionDTO> getAll() {
        return jobPositionMapper.toDtoList(jobPositionRepository.findAll());
    }

    @Override
    public List<JobPositionDTO> getAllActive() {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByActiveTrue());
    }

    @Override
    public List<JobPositionDTO> getByCompany(Long companyId) {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByCompanyId(companyId));
    }

    @Override
    public List<JobPositionDTO> getByCompanyActive(Long companyId) {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByCompanyIdAndActiveTrue(companyId));
    }

    @Override
    public List<JobPositionDTO> getByCategory(JobCategory category) {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByCategory(category));
    }

    @Override
    public List<JobPositionDTO> getByLevel(String level) {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByLevel(level));
    }

    @Override
    public List<JobPositionDTO> getByCareerPath(String careerPath) {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByCareerPath(careerPath));
    }

    @Override
    public List<JobPositionDTO> getByTitle(String title) {
        return jobPositionMapper.toDtoList(jobPositionRepository.findByTitleContainingIgnoreCase(title));
    }

    @Override
    public List<JobPositionDTO> getPositionsWithActiveVacancies() {
        return jobPositionMapper.toDtoList(jobPositionRepository.findPositionsWithActiveVacancies());
    }

    @Override
    public Integer countActiveVacanciesByPosition(Long positionId) {
        List<Object[]> results = jobPositionRepository.countActiveVacanciesByPosition();
        return results.stream()
                .filter(r -> ((Number) r[0]).longValue() == positionId)
                .map(r -> ((Number) r[1]).intValue())
                .findFirst()
                .orElse(0);
    }

    @Override
    public boolean existsByCompanyAndTitle(Long companyId, String title) {
        return jobPositionRepository.existsByCompanyIdAndTitleIgnoreCase(companyId, title);
    }
}