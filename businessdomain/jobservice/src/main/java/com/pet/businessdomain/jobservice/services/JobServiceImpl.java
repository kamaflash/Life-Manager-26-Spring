package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.dto.*;
import com.pet.businessdomain.jobservice.entities.*;
import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import com.pet.businessdomain.jobservice.entities.enumjobs.WorkModality;
import com.pet.businessdomain.jobservice.mapper.*;
import com.pet.businessdomain.jobservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// =======================
    @Service
    @Transactional
    public class JobServiceImpl implements IJobService {

        @Autowired private CompanyRepository companyRepository;
        @Autowired private JobPositionRepository positionRepository;
        @Autowired private JobVacancyRepository vacancyRepository;
        @Autowired private CharacterApplicationRepository applicationRepository;

        @Autowired private CompanyMapper companyMapper;
        @Autowired private JobPositionMapper positionMapper;
        @Autowired private JobVacancyMapper vacancyMapper;
        @Autowired private CharacterApplicationMapper applicationMapper;

        // =======================
        // 🏢 EMPRESAS
        // =======================
        @Override
        public List<CompanyDto> getAllCompanies() {
            return companyMapper.toDtoList(companyRepository.findAll());
        }

        @Override
        public CompanyDto getCompanyById(Long companyId) {
            return companyMapper.toDto(
                    companyRepository.findById(companyId)
                            .orElseThrow(() -> new RuntimeException("Company not found"))
            );
        }

        @Override
        public List<CompanyDto> getCompaniesByCategory(String category) {
            return companyMapper.toDtoList(companyRepository.findByCategory(category));
        }
    @Override
    public Page<JobPositionEntity> getFilteredPositions(
            JobCategory category,
            String city,
            List<String> userSkills, // ahora solo para posibles futuros filtros
            int minMatch,
            int userXpAcademy,
            int userXpJobs,
            Pageable pageable
    ) {

        Page<JobPositionEntity> page =
                positionRepository.findByCategoryAndActiveTrue(category, pageable);

        List<JobPositionEntity> filtered = page.getContent().stream()
                // mapear cada job con su match temporal
                .map(job -> {
                    int characterXp = userXpAcademy + userXpJobs;

                    int requiredXp = Math.max(
                            job.getMinXp() != null ? job.getMinXp() : 0,
                            job.getExperienceRequired() != null && job.getExperienceRequired().getMinXp() != null
                                    ? job.getExperienceRequired().getMinXp()
                                    : 0
                    );

                    int match = characterXp < requiredXp ? 0 : 100;

                    return new AbstractMap.SimpleEntry<>(job, match);
                })
                // filtrar según match y ubicación/remoto
                .filter(entry -> {
                    JobPositionEntity job = entry.getKey();
                    int match = entry.getValue();

                    boolean isRemote = "REMOTE".equalsIgnoreCase(job.getWorkModality());

                    boolean sameCity = job.getLocation() != null &&
                            job.getLocation().toLowerCase().contains(city.toLowerCase());

                    return match >= minMatch && (isRemote || sameCity);
                })
                // ordenar descendente por match
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                // extraer solo los jobs, sin el match
                .map(Map.Entry::getKey)
                .toList();

        return new PageImpl<>(filtered, pageable, filtered.size());
    }



    @Override
        public CompanyDto createCompany(CompanyDto dto) {
            CompanyEntity entity = companyMapper.toEntity(dto);
            entity.setId(dto.getId());
            return companyMapper.toDto(companyRepository.save(entity));
        }

        @Override
        public CompanyDto updateCompany(Long id, CompanyDto dto) {
            CompanyEntity entity = companyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Company not found"));

            companyMapper.updateEntity(dto, entity);
            return companyMapper.toDto(companyRepository.save(entity));
        }

        @Override
        public void deleteCompany(Long id) {
            companyRepository.deleteById(id);
        }

        // =======================
        // 💼 PUESTOS
        // =======================
        @Override
        public List<JobPositionDto> getPositionsByCompany(Long companyId) {
            return positionMapper.toDtoList(
                    positionRepository.findByCompanyId(companyId)
            );
        }

        @Override
        public JobPositionDto getPositionById(Long positionId) {
            return positionMapper.toDto(
                    positionRepository.findById(positionId)
                            .orElseThrow(() -> new RuntimeException("Position not found"))
            );
        }

        @Override
        public List<JobPositionDto> getPositionsByCategory(JobCategory category) {
            return positionMapper.toDtoList(
                    positionRepository.findByCategoryOrOther(category)
            );
        }
    @Override
    public Page<JobPositionEntity> getPositionsByCategoryPage(Pageable pageable, JobCategory category) {
            return positionRepository.findByCategoryAndActiveTrue(category, pageable);
    }
        @Override
        public JobPositionDto createPosition(JobPositionDto dto) {

            JobPositionEntity entity = positionMapper.toEntity(dto);
            entity.setId(dto.getId());
            entity.setActive(true);

            // 👉 Vincular empresa
            CompanyEntity company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found"));
            entity.setCompany(company);

            // 👉 Vincular experiencia (lado propietario)
            if (entity.getExperienceRequired() != null) {
                entity.getExperienceRequired().setJobPosition(entity);
            }

            return positionMapper.toDto(positionRepository.save(entity));
        }

        @Override
        public JobPositionDto updatePosition(Long id, JobPositionDto dto) {
            JobPositionEntity entity = positionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Position not found"));

            positionMapper.updateEntity(dto, entity);

            if (dto.getCompanyId() != null) {
                CompanyEntity company = companyRepository.findById(dto.getCompanyId())
                        .orElseThrow(() -> new RuntimeException("Company not found"));
                entity.setCompany(company);
            }

            if (entity.getExperienceRequired() != null) {
                entity.getExperienceRequired().setJobPosition(entity);
            }

            return positionMapper.toDto(positionRepository.save(entity));
        }

        @Override
        public void deletePosition(Long id) {
            positionRepository.deleteById(id);
        }

        // =======================
        // 📌 VACANTES
        // =======================
        @Override
        public List<JobVacancyDto> getVacanciesByPosition(Long positionId) {
            return vacancyMapper.toDtoList(
                    vacancyRepository.findByPositionId(positionId)
            );
        }

        @Override
        public JobVacancyDto getVacancyById(Long vacancyId) {
            return vacancyMapper.toDto(
                    vacancyRepository.findById(vacancyId)
                            .orElseThrow(() -> new RuntimeException("Vacancy not found"))
            );
        }

        @Override
        public List<VacancyFullDto> getAllFullVacancies() {

            List<VacancyFullDto> dtos = vacancyRepository.findAllFullVacancies();

            // Mapear listas de skills manualmente
            for (VacancyFullDto dto : dtos) {
                // Obtenemos la posición para extraer listas
                JobPositionEntity position = positionRepository.findById(dto.getPositionId())
                        .orElse(null);
                if (position != null && position.getExperienceRequired() != null) {
                    dto.setRequiredSkills(position.getExperienceRequired().getRequiredSkills());
                    dto.setOptionalSkills(position.getExperienceRequired().getOptionalSkills());
                }
            }

            return dtos;
        }

        @Override
        public JobVacancyDto createVacancy(JobVacancyDto dto) {

            JobVacancyEntity entity = vacancyMapper.toEntity(dto);
            entity.setId(dto.getId());
            JobPositionEntity position = positionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));

            entity.setPosition(position);

            return vacancyMapper.toDto(vacancyRepository.save(entity));
        }

        @Override
        public JobVacancyDto updateVacancy(Long id, JobVacancyDto dto) {
            JobVacancyEntity entity = vacancyRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Vacancy not found"));

            vacancyMapper.updateEntity(dto, entity);

            return vacancyMapper.toDto(vacancyRepository.save(entity));
        }

        @Override
        public void deleteVacancy(Long id) {
            vacancyRepository.deleteById(id);
        }

        // =======================
        // 📝 APLICACIONES
        // =======================
        @Override
        public List<CharacterApplicationDto> getApplicationsByCharacter(Long characterId) {
            return applicationMapper.toDtoList(
                    applicationRepository.findByCharacterId(characterId)
            );
        }

        @Override
        public CharacterApplicationDto applyToVacancy(CharacterApplicationDto dto) {
            CharacterApplicationEntity entity = applicationMapper.toEntity(dto);
            entity.setId(null);
            entity.setAppliedAt(LocalDateTime.now());
            entity.setStatus("APPLIED");

            return applicationMapper.toDto(applicationRepository.save(entity));
        }

        @Override
        public void cancelApplication(Long applicationId) {
            applicationRepository.deleteById(applicationId);
        }

        @Override
        public List<JobVacancyDto> getAvailableVacanciesForCharacter(Long characterId) {
            return List.of();
        }

        @Override
        public void cancelAll() {
            vacancyRepository.deleteAll();
            positionRepository.deleteAll();
            companyRepository.deleteAll();
        }
        // =======================
        // 🧠 LÓGICA AVANZADA
        // =======================
//        @Override
//        public List<JobVacancyDto> getAvailableVacanciesForCharacter(Long characterId) {
//            return vacancyMapper.toDtoList(
//                    vacancyRepository.findByActiveTrue()
//            );
//        }

    private int calculateMatchPercentage(List<String> userSkills, List<String> jobSkills) {

        if (jobSkills == null || jobSkills.isEmpty()) return 0;

        Set<String> userSkillSet = userSkills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        long matches = jobSkills.stream()
                .map(String::toLowerCase)
                .filter(userSkillSet::contains)
                .count();

        return (int) ((matches * 100.0) / jobSkills.size());
    }

}

