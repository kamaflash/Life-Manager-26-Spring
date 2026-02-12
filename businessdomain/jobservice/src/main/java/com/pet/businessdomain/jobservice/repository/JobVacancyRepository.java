package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.dto.VacancyFullDto;
import com.pet.businessdomain.jobservice.entities.JobVacancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobVacancyRepository extends JpaRepository<JobVacancyEntity, Long> {

    // Vacantes de un puesto específico
    List<JobVacancyEntity> findByPositionId(Long positionId);

    @Query("""
SELECT new com.pet.businessdomain.jobservice.dto.VacancyFullDto(
    v.id,
    p.id,
    p.title,
    p.description,
    p.title,
    e.minYears,
    e.minLevel,
    e.minXp,
    c.name,
    v.salary
)
FROM JobVacancyEntity v
JOIN v.position p
JOIN p.company c
LEFT JOIN p.experienceRequired e
""")
    List<VacancyFullDto> findAllFullVacancies();

}
