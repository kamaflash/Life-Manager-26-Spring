package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import com.pet.businessdomain.jobservice.mapper.CompanyMapper;
import com.pet.businessdomain.jobservice.repository.CompanyRepository;
import com.pet.businessdomain.shareddto.dto.CompanyDTO;
import com.pet.businessdomain.shareddto.dto.CompanySummaryDTO;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    @Transactional
    public CompanyDTO create(CompanyDTO dto) {
        log.info("Creating new company: {}", dto.getName());

        CompanyEntity entity = companyMapper.toEntity(dto);
        entity.setActive(true);

        CompanyEntity saved = companyRepository.save(entity);
        return companyMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CompanyDTO update(Long id, CompanyDTO dto) {
        log.info("Updating company with id: {}", id);

        CompanyEntity entity = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));

        companyMapper.updateEntity(dto, entity);
        CompanyEntity updated = companyRepository.save(entity);
        return companyMapper.toDto(updated);
    }

    @Override
    public CompanyDTO getById(Long id) {
        return companyRepository.findById(id)
                .map(companyMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Override
    public CompanySummaryDTO getSummaryById(Long id) {
        return companyRepository.findById(id)
                .map(companyMapper::toSummaryDto)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting company with id: {}", id);
        companyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating company with id: {}", id);
        CompanyEntity entity = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
        entity.setActive(false);
        companyRepository.save(entity);
    }

    @Override
    public List<CompanyDTO> getAll() {
        return companyMapper.toDtoList(companyRepository.findAll());
    }

    @Override
    public List<CompanyDTO> getAllActive() {
        return companyMapper.toDtoList(companyRepository.findByActiveTrue());
    }

    @Override
    public List<CompanySummaryDTO> getAllSummaries() {
        return companyMapper.toSummaryDtoList(companyRepository.findAll());
    }

    @Override
    public List<CompanyDTO> getByCategory(JobCategory category) {
        return companyMapper.toDtoList(companyRepository.findByCategory(category));
    }

    @Override
    public List<CompanyDTO> getByLocation(String location) {
        return companyMapper.toDtoList(companyRepository.findByLocationContainingIgnoreCase(location));
    }

//    @Override
//    public List<CompanyDTO> getByReputationGreaterThan(Integer reputation) {
//        return companyMapper.toDtoList(companyRepository.findByReputationGreaterThan(reputation));
//    }

    @Override
    public List<CompanyDTO> getRemoteFriendly() {
        return companyMapper.toDtoList(companyRepository.findByRemoteFriendlyTrue());
    }

    @Override
    public List<CompanyDTO> getWithInternships() {
        return companyMapper.toDtoList(companyRepository.findByInternshipAvailableTrue());
    }

    @Override
    public List<CompanyDTO> search(String name, JobCategory category, String location, Boolean active) {
        return companyMapper.toDtoList(companyRepository.searchCompanies(name, category, location, active));
    }

    @Override
    public List<CompanyDTO> findTopCompaniesByVacancies() {
        return companyMapper.toDtoList(companyRepository.findTopCompaniesByVacancies());
    }

    @Override
    public Integer countActiveVacanciesByCompany(Long companyId) {
        List<Object[]> results = companyRepository.countActiveVacanciesByCompany();
        return results.stream()
                .filter(r -> ((Number) r[0]).longValue() == companyId)
                .map(r -> ((Number) r[1]).intValue())
                .findFirst()
                .orElse(0);
    }

    @Override
    public Long countEmployees(Long companyId) {
        // Implementación que cuenta empleados activos en la empresa
        return 0L; // Placeholder - implementar según necesidad
    }
}