package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.jobservice.entities.RequirementEntity;
import com.pet.businessdomain.jobservice.mapper.JobVacancyMapper;
import com.pet.businessdomain.jobservice.repository.JobPositionRepository;
import com.pet.businessdomain.jobservice.repository.JobVacancyRepository;
import com.pet.businessdomain.jobservice.services.JobVacancyService;
import com.pet.businessdomain.shareddto.dto.JobSearchFiltersDTO;
import com.pet.businessdomain.shareddto.dto.JobVacancyDTO;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobVacancyServiceImpl implements JobVacancyService {

    private final JobVacancyRepository jobVacancyRepository;
    private final JobPositionRepository jobPositionRepository;
    private final JobVacancyMapper jobVacancyMapper;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public JobVacancyDTO create(JobVacancyDTO dto) {
        log.info("Creating new job vacancy for position: {}", dto.getPositionId());

        JobVacancyEntity entity = jobVacancyMapper.toEntity(dto);

        // ✅ Asignar la relación con JobPosition
        if (dto.getPositionId() != null) {
            JobPositionEntity position = jobPositionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Job position not found with id: " + dto.getPositionId()));
            entity.setPosition(position);
        }

        JobVacancyEntity saved = jobVacancyRepository.save(entity);
        return jobVacancyMapper.toDto(saved);
    }

    @Override
    @Transactional
    public JobVacancyDTO update(Long id, JobVacancyDTO dto) {
        log.info("Updating job vacancy with id: {}", id);

        JobVacancyEntity entity = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job vacancy not found with id: " + id));

        jobVacancyMapper.updateEntity(dto, entity);
        JobVacancyEntity updated = jobVacancyRepository.save(entity);
        return jobVacancyMapper.toDto(updated);
    }

    @Override
    public JobVacancyDTO getById(Long id) {
        // ✅ Usar método con JOIN FETCH para cargar relaciones
        JobVacancyEntity entity = jobVacancyRepository.findByIdWithRelations(id);
        if (entity == null) {
            throw new RuntimeException("Job vacancy not found with id: " + id);
        }
        return jobVacancyMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting job vacancy with id: {}", id);
        jobVacancyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void closeVacancy(Long id) {
        log.info("Closing job vacancy with id: {}", id);
        JobVacancyEntity entity = jobVacancyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job vacancy not found with id: " + id));
        entity.setActive(false);
        entity.setClosingDate(LocalDate.now());
        jobVacancyRepository.save(entity);
    }

    @Override
    public List<JobVacancyDTO> getAll() {
        // ✅ Usar método con JOIN FETCH para cargar relaciones
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findAllWithRelations());
    }

    @Override
    public List<JobVacancyDTO> getAllActive() {
        // ✅ Usar método con JOIN FETCH para cargar relaciones
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByActiveTrueWithRelations());
    }

    @Override
    public List<JobVacancyDTO> getByPosition(Long positionId) {
        // ✅ Usar método con JOIN FETCH para cargar relaciones
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByPositionIdWithRelations(positionId));
    }

    @Override
    public List<JobVacancyDTO> getByPositionActive(Long positionId) {
        // ✅ Usar método con JOIN FETCH para cargar relaciones
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByPositionIdAndActiveTrueWithRelations(positionId));
    }

    @Override
    public List<JobVacancyDTO> getByCompany(Long companyId) {
        // ✅ Usar método con JOIN FETCH para cargar relaciones
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByCompanyIdWithRelations(companyId));
    }

    @Override
    public List<JobVacancyDTO> getByContractType(String contractType) {
        // ⚠️ Este método no necesita JOIN FETCH porque solo devuelve datos básicos
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByContractType(contractType));
    }

    @Override
    public List<JobVacancyDTO> getByWorkModality(String workModality) {
        // ⚠️ Este método no necesita JOIN FETCH porque solo devuelve datos básicos
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByWorkModality(workModality));
    }


    @Override
    public List<JobVacancyDTO> getBySalaryRange(BigDecimal min, BigDecimal max) {
        // ⚠️ Este método no necesita JOIN FETCH porque solo devuelve datos básicos
        return jobVacancyMapper.toDtoList(jobVacancyRepository.findByMinSalaryBetween(min, max));
    }

//    @Override
//    @Transactional  // ← Importante: mantener la transacción abierta
//    public List<JobVacancyDTO> search(JobSearchFiltersDTO filters) {
//        List<JobVacancyEntity> entities = jobVacancyRepository.searchVacancies(
//                filters.getKeyword(),
//                filters.getContractTypes() != null && !filters.getContractTypes().isEmpty() ?
//                        filters.getContractTypes().get(0) : null,
//                filters.getWorkModalities() != null && !filters.getWorkModalities().isEmpty() ?
//                        filters.getWorkModalities().get(0) : null,
//                filters.getMinSalary() != null ? BigDecimal.valueOf(filters.getMinSalary()) : null,
//                filters.getMaxSalary() != null ? BigDecimal.valueOf(filters.getMaxSalary()) : null
//        );
//
//        // Forzar carga de relaciones dentro de la transacción
//        for (JobVacancyEntity entity : entities) {
//            Hibernate.initialize(entity.getPosition());
//            if (entity.getPosition() != null) {
//                Hibernate.initialize(entity.getPosition().getCompany());
//            }
//            Hibernate.initialize(entity.getRequirements());
//        }
//
//        return jobVacancyMapper.toDtoList(entities);
//    }


    @Override
    @Transactional
    public Page<JobVacancyDTO> search(JobSearchFiltersDTO filters, Pageable pageable) {

        // Keyword
        String keyword = filters.getKeywordAsString();

        // Convertir listas a Strings con separador '|' para búsqueda OR
        String positionTitles = null;
        if (filters.getPositionTitles() != null && !filters.getPositionTitles().isEmpty()) {
            positionTitles = String.join("|", filters.getPositionTitles());
        }

        String companyNames = null;
        if (filters.getCompanyNames() != null && !filters.getCompanyNames().isEmpty()) {
            companyNames = String.join("|", filters.getCompanyNames());
        }

        String categories = null;
        List<String> categoryList = filters.getCategoriesList();
        if (categoryList != null && !categoryList.isEmpty()) {
            categories = String.join("|", categoryList);
        }

        String contractTypes = null;
        if (filters.getContractTypes() != null && !filters.getContractTypes().isEmpty()) {
            contractTypes = String.join("|", filters.getContractTypes());
        }

        String workModalities = null;
        if (filters.getWorkModalities() != null && !filters.getWorkModalities().isEmpty()) {
            workModalities = String.join("|", filters.getWorkModalities());
        }

        String weeklyHours = null;
        if (filters.getWeeklyHours() != null && !filters.getWeeklyHours().isEmpty()) {
            weeklyHours = String.join("|", filters.getWeeklyHours().stream().map(String::valueOf).toArray(String[]::new));
        }

        String schedule = null;
        if (filters.getSchedule() != null && !filters.getSchedule().isEmpty()) {
            schedule = filters.getSchedule();
        }

        Integer minAvailableSlots = filters.getAvailableSlots() != null ? filters.getAvailableSlots() : null;

        BigDecimal minSalary = filters.getMinSalary() != null ? filters.getMinSalary() : null;
        BigDecimal maxSalary = filters.getMaxSalary() != null ? filters.getMaxSalary() : null;

        // 🔥 Usar el flag enviado desde el frontend
        boolean onlyWeekendJobs = filters.getOnlyWeekendJobs() != null && filters.getOnlyWeekendJobs();

        log.info("Only weekend jobs filter: {}", onlyWeekendJobs);

        // Usar Specifications
        Page<JobVacancyEntity> entityPage = jobVacancyRepository.searchVacanciesPage(
                keyword,
                positionTitles,
                companyNames,
                categories,
                contractTypes,
                workModalities,
                weeklyHours,
                schedule,
                minAvailableSlots,
                minSalary,
                maxSalary,
                pageable
        );

        // 🔥 Cargar las colecciones en consultas separadas
        if (!entityPage.getContent().isEmpty()) {
            List<Long> vacancyIds = entityPage.getContent().stream()
                    .map(JobVacancyEntity::getId)
                    .collect(Collectors.toList());

            // Cargar workingDays
            Map<Long, List<EnumAll.WorkingDay>> workingDaysMap = loadWorkingDays(vacancyIds);

            // Cargar benefits
            Map<Long, List<String>> benefitsMap = loadBenefits(vacancyIds);

            // Cargar requirements
            Map<Long, List<RequirementEntity>> requirementsMap = loadRequirements(vacancyIds);

            // Actualizar colecciones
            for (JobVacancyEntity vacancy : entityPage.getContent()) {
                if (workingDaysMap.containsKey(vacancy.getId())) {
                    vacancy.getWorkingDays().clear();
                    vacancy.getWorkingDays().addAll(workingDaysMap.get(vacancy.getId()));
                }
                if (benefitsMap.containsKey(vacancy.getId())) {
                    vacancy.getBenefits().clear();
                    vacancy.getBenefits().addAll(benefitsMap.get(vacancy.getId()));
                }
                if (requirementsMap.containsKey(vacancy.getId())) {
                    vacancy.getRequirements().clear();
                    for (RequirementEntity req : requirementsMap.get(vacancy.getId())) {
                        vacancy.addRequirement(req);
                    }
                }
            }

            // 🔥 Filtrar trabajos según el flag onlyWeekendJobs
            if (onlyWeekendJobs) {
                List<JobVacancyEntity> filteredContent = entityPage.getContent().stream()
                        .filter(vacancy -> isWeekendOnlyJob(vacancy))
                        .collect(Collectors.toList());

                log.info("Filtered to {} weekend-only jobs out of {}", filteredContent.size(), entityPage.getContent().size());

                entityPage = new PageImpl<>(filteredContent, pageable, filteredContent.size());
            }
        }

        return entityPage.map(jobVacancyMapper::toDto);
    }

    /**
     * Verifica si un trabajo es solo de fin de semana
     */
    private boolean isWeekendOnlyJob(JobVacancyEntity vacancy) {
        if (vacancy.getWorkingDays() == null || vacancy.getWorkingDays().isEmpty()) {
            // Si no tiene workingDays definidos, asumimos que es de lunes a viernes
            return false;
        }

        // Verificar que SOLO tenga sábado y domingo (y no otros días)
        boolean hasSaturday = vacancy.getWorkingDays().contains(EnumAll.WorkingDay.SATURDAY);
        boolean hasSunday = vacancy.getWorkingDays().contains(EnumAll.WorkingDay.SUNDAY);
        boolean hasOtherDays = vacancy.getWorkingDays().stream()
                .anyMatch(day -> day != EnumAll.WorkingDay.SATURDAY && day != EnumAll.WorkingDay.SUNDAY);

        return hasSaturday && hasSunday && !hasOtherDays;
    }

    private Map<Long, List<EnumAll.WorkingDay>> loadWorkingDays(List<Long> vacancyIds) {
        if (vacancyIds == null || vacancyIds.isEmpty()) {
            return new HashMap<>();
        }

        List<Object[]> results = entityManager.createQuery(
                "SELECT v.id, wd FROM JobVacancyEntity v JOIN v.workingDays wd WHERE v.id IN :ids",
                Object[].class
        ).setParameter("ids", vacancyIds).getResultList();

        Map<Long, List<EnumAll.WorkingDay>> map = new HashMap<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            EnumAll.WorkingDay day = (EnumAll.WorkingDay) result[1];
            map.computeIfAbsent(id, k -> new ArrayList<>()).add(day);
        }
        return map;
    }

    private Map<Long, List<String>> loadBenefits(List<Long> vacancyIds) {
        if (vacancyIds == null || vacancyIds.isEmpty()) {
            return new HashMap<>();
        }

        List<Object[]> results = entityManager.createQuery(
                "SELECT v.id, b FROM JobVacancyEntity v JOIN v.benefits b WHERE v.id IN :ids",
                Object[].class
        ).setParameter("ids", vacancyIds).getResultList();

        Map<Long, List<String>> map = new HashMap<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            String benefit = (String) result[1];
            map.computeIfAbsent(id, k -> new ArrayList<>()).add(benefit);
        }
        return map;
    }

    private Map<Long, List<RequirementEntity>> loadRequirements(List<Long> vacancyIds) {
        if (vacancyIds == null || vacancyIds.isEmpty()) {
            return new HashMap<>();
        }

        List<Object[]> results = entityManager.createQuery(
                "SELECT v.id, r FROM JobVacancyEntity v JOIN v.requirements r WHERE v.id IN :ids",
                Object[].class
        ).setParameter("ids", vacancyIds).getResultList();

        Map<Long, List<RequirementEntity>> map = new HashMap<>();
        for (Object[] result : results) {
            Long id = (Long) result[0];
            RequirementEntity requirement = (RequirementEntity) result[1];
            map.computeIfAbsent(id, k -> new ArrayList<>()).add(requirement);
        }
        return map;
    }

    @Override
    public List<JobVacancyDTO> findExpiringSoon() {
        // ⚠️ Este método necesita JOIN FETCH para mostrar datos completos
        List<JobVacancyEntity> entities = jobVacancyRepository.findExpiringSoon();

        // Cargar relaciones manualmente para cada entidad
        for (JobVacancyEntity entity : entities) {
            if (entity.getPosition() != null && entity.getPosition().getCompany() != null) {
                entity.getPosition().getCompany().getName();
            }
        }

        return jobVacancyMapper.toDtoList(entities);
    }

    @Override
    public Integer countApplicants(Long vacancyId) {
        List<Object[]> results = jobVacancyRepository.countApplicantsByVacancy();
        return results.stream()
                .filter(r -> ((Number) r[0]).longValue() == vacancyId)
                .map(r -> ((Number) r[1]).intValue())
                .findFirst()
                .orElse(0);
    }

    @Override
    public Integer getAvailableSlots(Long vacancyId) {
        return jobVacancyRepository.findById(vacancyId)
                .map(JobVacancyEntity::getAvailableSlots)
                .orElse(0);
    }

    @Override
    public boolean hasAvailableSlots(Long vacancyId) {
        Integer slots = getAvailableSlots(vacancyId);
        return slots != null && slots > 0;
    }

    @Override
    public boolean isExpired(Long vacancyId) {
        return jobVacancyRepository.findById(vacancyId)
                .map(v -> v.getClosingDate() != null && v.getClosingDate().isBefore(LocalDate.now()))
                .orElse(true);
    }

    @Override
    public boolean isActive(Long vacancyId) {
        return jobVacancyRepository.findById(vacancyId)
                .map(JobVacancyEntity::getActive)
                .orElse(false);
    }
}