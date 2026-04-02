package com.pet.businessdomain.formationservice.repository;

import com.pet.businessdomain.formationservice.entities.ScholarshipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScholarshipRepository
        extends JpaRepository<ScholarshipEntity, Long> {

    List<ScholarshipEntity> findByActiveTrue();

}
