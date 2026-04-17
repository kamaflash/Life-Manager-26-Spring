package com.pet.businessdomain.jobservice.mapper;

import com.pet.businessdomain.jobservice.entities.CharacterJobEntity;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import com.pet.businessdomain.shareddto.dto.CharacterJobDTO;
import com.pet.businessdomain.shareddto.dto.JobVacancyDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CharacterJobMapper {

    // ENTITY → DTO
    @Mapping(target = "vacancy", source = "vacancy", qualifiedByName = "mapVacancyToDto")
    @Mapping(source = "vacancy.position.title", target = "positionTitle")
    @Mapping(source = "vacancy.position.company.name", target = "companyName")
    @Mapping(source = "vacancy.position.company.logoUrl", target = "companyLogo")
    @Mapping(source = "vacancy.minSalary", target = "currentSalary")
    CharacterJobDTO toDto(CharacterJobEntity entity);

    List<CharacterJobDTO> toDtoList(List<CharacterJobEntity> entities);

    // DTO → ENTITY (CREATE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vacancy", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "performance", constant = "50")
    @Mapping(target = "satisfaction", constant = "50")
    @Mapping(target = "stressLevel", constant = "30")
    @Mapping(target = "promotionsReceived", constant = "0")
    @Mapping(target = "bonusesReceived", constant = "0")
    CharacterJobEntity toEntity(CharacterJobDTO dto);

    // DTO → ENTITY (UPDATE)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vacancy", ignore = true)
    void updateEntity(CharacterJobDTO dto, @MappingTarget CharacterJobEntity entity);

    // Mapea JobVacancyEntity → JobVacancyDTO
    @Named("mapVacancyToDto")
    default JobVacancyDTO mapVacancyToDto(JobVacancyEntity entity) {
        if (entity == null) return null;

        JobVacancyDTO dto = new JobVacancyDTO();

        // Datos básicos
        dto.setId(entity.getId());
        dto.setActive(entity.getActive());
        dto.setOpeningDate(entity.getOpeningDate());
        dto.setClosingDate(entity.getClosingDate());
        dto.setAvailableSlots(entity.getAvailableSlots());

        // Salario
        dto.setMinSalary(entity.getMinSalary());
        dto.setMaxSalary(entity.getMaxSalary());

        // Detalles del puesto
        dto.setContractType(String.valueOf(entity.getContractType()));
        dto.setWorkModality(String.valueOf(entity.getWorkModality()));
        dto.setVisaSponsorship(entity.getVisaSponsorship());
        dto.setWeeklyHours(entity.getWeeklyHours());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setDescription(entity.getDescription());

        // Listas
        dto.setWorkingDays(entity.getWorkingDays());
        dto.setBenefits(entity.getBenefits());

        // Datos de posición y empresa (desde las relaciones)
        if (entity.getPosition() != null) {
            dto.setPositionId(entity.getPosition().getId());
            dto.setPositionTitle(entity.getPosition().getTitle());

            if (entity.getPosition().getCompany() != null) {
                dto.setCompanyName(entity.getPosition().getCompany().getName());
                dto.setCompanyLogo(entity.getPosition().getCompany().getLogoUrl());
            }
        }

        // Requirements (si los tienes)
        if (entity.getRequirements() != null) {
            // Mapear Requirements a RequirementDTO si es necesario
            // dto.setRequirements(mapRequirements(entity.getRequirements()));
        }

        // Estadísticas (si las tienes)
        // dto.setTotalApplicants(entity.getTotalApplicants());
        // dto.setMatchScore(entity.getMatchScore());

        return dto;
    }
}