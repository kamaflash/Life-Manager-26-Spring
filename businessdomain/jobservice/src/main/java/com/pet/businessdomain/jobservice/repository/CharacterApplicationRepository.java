package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.CharacterApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CharacterApplicationRepository extends JpaRepository<CharacterApplicationEntity, Long> {

    // Aplicaciones de un personaje específico
    List<CharacterApplicationEntity> findByCharacterId(Long characterId);

    // Aplicaciones de un personaje a una vacante específica
    List<CharacterApplicationEntity> findByCharacterIdAndVacancyId(Long characterId, Long vacancyId);
    @Query("SELECT a.vacancyId FROM CharacterApplicationEntity a WHERE a.characterId = :pid")
    List<Long> findVacancyIdsByCharacterId(@Param("pid") Long pid);
    // Aplicaciones por estado
    List<CharacterApplicationEntity> findByStatus(String status);
}
