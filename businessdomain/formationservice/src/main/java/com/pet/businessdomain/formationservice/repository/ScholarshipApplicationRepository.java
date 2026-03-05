package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.ScholarshipApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScholarshipApplicationRepository
        extends JpaRepository<ScholarshipApplicationEntity, Long> {

    List<ScholarshipApplicationEntity> findByCharacterId(Long characterId);

    List<ScholarshipApplicationEntity> findByScholarshipId(Long scholarshipId);

    Optional<ScholarshipApplicationEntity> findByScholarshipIdAndCharacterId(Long scholarshipId, Long characterId);
}
