package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.CharacterApplicationEntity;
import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.shareddto.dto.*;
import com.pet.businessdomain.shareddto.enumentities.*;
import com.pet.businessdomain.jobservice.mapper.CharacterApplicationMapper;
import com.pet.businessdomain.jobservice.mapper.CompanyMapper;
import com.pet.businessdomain.jobservice.mapper.JobPositionMapper;
import com.pet.businessdomain.jobservice.mapper.JobVacancyMapper;
import com.pet.businessdomain.jobservice.repository.CharacterApplicationRepository;
import com.pet.businessdomain.jobservice.repository.CompanyRepository;
import com.pet.businessdomain.jobservice.repository.JobPositionRepository;
import com.pet.businessdomain.jobservice.repository.JobVacancyRepository;
import com.pet.businessdomain.jobservice.transactions.BusinessTransactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// =======================
@Service
@Transactional
public class JobServiceImpl implements IJobService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JobPositionRepository positionRepository;
    @Autowired
    private JobVacancyRepository vacancyRepository;
    @Autowired
    private CharacterApplicationRepository applicationRepository;

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private JobPositionMapper positionMapper;
    @Autowired
    private JobVacancyMapper vacancyMapper;
    @Autowired
    private CharacterApplicationMapper applicationMapper;
    @Autowired
    private BusinessTransactions businessTransactions;

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
            List<String> userSkills, // ahora solo para posibles futuros filtros
            int minMatch,
            Long pid,
            Pageable pageable
    ) {
        CharacterDto characterDto = businessTransactions.getCharacter(pid);
        Page<JobPositionEntity> page =
                positionRepository.findByCategoryAndActiveTrue(category, pageable);
        Set<Long> takenJobIds = characterDto.getJobs() == null
                ? Collections.emptySet()
                : characterDto.getJobs().stream()
                .map(JobPositionDto::getId) // ajusta si no es DTO
                .collect(Collectors.toSet());

        List<JobPositionEntity> filtered = page.getContent().stream()

                .map(job -> {

                    List<JobVacancyDto> vacancies =
                            getVacanciesByPosition(job.getId());

                    int characterXp = characterDto.getXpAcademy() + characterDto.getXpAcademy();

                    int requiredXp = Math.max(
                            job.getMinXp() != null ? job.getMinXp() : 0,
                            job.getExperienceRequired() != null &&
                                    job.getExperienceRequired().getMinXp() != null
                                    ? job.getExperienceRequired().getMinXp()
                                    : 0
                    );

                    int match = characterXp < requiredXp ? 0 : 100;

                    return new Object[] { job, match, vacancies };
                })

                .filter(obj -> {

                    JobPositionEntity job = (JobPositionEntity) obj[0];
                    int match = (int) obj[1];
                    List<JobVacancyDto> vacancies = (List<JobVacancyDto>) obj[2];

                    boolean isRemote = "REMOTE".equalsIgnoreCase(job.getWorkModality());

                    boolean sameCity = job.getLocation() != null &&
                            job.getLocation().toLowerCase()
                                    .contains(characterDto.getCity().toLowerCase());

                    boolean notAlreadyTaken = !takenJobIds.contains(job.getId());

                    boolean notAlreadyApplied = true;

                    if (vacancies != null && !vacancies.isEmpty()) {

                        List<Long> vacancyIds = vacancies.stream()
                                .map(JobVacancyDto::getId)
                                .toList();

                        notAlreadyApplied = !isJobs(pid, vacancyIds);
                    }

                    return match >= minMatch
                            && (isRemote || sameCity)
                            && notAlreadyTaken
                            && notAlreadyApplied;
                })
                .map(obj -> (JobPositionEntity) obj[0])
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
        JobPositionEntity position = positionRepository
                .findPositionByVacancyId(dto.getVacancyId())
                .orElseThrow(() -> new RuntimeException("Position not found"));
        CharacterApplicationEntity entity = applicationMapper.toEntity(dto);
        entity.setId(null);
        entity.setAppliedAt(LocalDateTime.now());
        entity.setStatus("APPLIED");
        entity.setPositionId(position.getId());
        entity = applicationRepository.save(entity);
        updateSystem(entity);
        addIncome(dto,entity);
        dto.setId(entity.getCharacterId());
        setNotification(dto);
        return applicationMapper.toDto(entity);
    }
    private void  updateSystem(CharacterApplicationEntity entity) {
        CharacterDto characterDto = businessTransactions.getCharacter(entity.getCharacterId());
        SystemDto systemDto = businessTransactions.getSystem(characterDto.getUid());
        LocalDateTime current = systemDto.getActualityAt();
        LocalDateTime newActuality = current.plusHours(2);
        systemDto.setActualityAt(newActuality);
        systemDto = businessTransactions.updateSystem(systemDto.getUid(),newActuality);
    }
    private void addIncome(CharacterApplicationDto dto, CharacterApplicationEntity entity) {
        CharacterDto characterDto = businessTransactions.getCharacter(dto.getCharacterId());
        List<CharacterApplicationDto> jobs = getApplicationsByCharacter(dto.getCharacterId());
        if (characterDto.getJobs().isEmpty()) {
            IncomeResponseDto incomeResponseDto = new IncomeResponseDto();
            incomeResponseDto.setAmount(BigDecimal.valueOf(100));
            incomeResponseDto.setSource("Enorabuena, premio metalico de Life manager por primer trabajo");
            incomeResponseDto.setFrequency(EnumAll.Frequency.OTHER);
            incomeResponseDto.setExternalRefId(entity.getId());
            incomeResponseDto.setExternalRefType("Permio Life manager");
            incomeResponseDto = businessTransactions.setIncome(incomeResponseDto, characterDto.getAccounts().get(0).getId());
        }
    }
    @Override
    public FinanceAccountResponseDto getAccountByCharacterId(Long characterId) {
        return businessTransactions.getAccount(characterId);
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


    private boolean isJobs(Long pid, List<Long> vip) {

        if (vip == null || vip.isEmpty()) {
            return false;
        }

        Set<Long> vipSet = new HashSet<>(vip);

        return applicationRepository.findVacancyIdsByCharacterId(pid)
                .stream()
                .anyMatch(vipSet::contains);
    }

    private void setNotification(CharacterApplicationDto dto) {

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setUserId(dto.getId());
        notificationDTO.setFromUserId(dto.getCharacterId());
        notificationDTO.setType(NotificationType.SYSTEM);

        notificationDTO.setTitle("Nuevo trabajo");
        notificationDTO.setSubTitle("Has aplicado a un nuevo puesto de trabajo");
        notificationDTO.setMessage("Has aplicado a un nuevo puesto de trabajo.");

        // 🔥 Nuevo sistema
        notificationDTO.setResourceType(NotificationResourceType.JOBS);
        notificationDTO.setResourceId(dto.getVacancyId());

        // 🔥 Navegación directa frontend
        notificationDTO.setActionUrl("/profile" );

        // 🔥 Metadata (opcional pero muy recomendable)
        notificationDTO.setMetadata("""
        {
            "characterId": %d
        }
    """.formatted(dto.getId()));

        notificationDTO.setRead(false);

        NotificationDTO resp = businessTransactions.setNotifications(notificationDTO);
    }
}

