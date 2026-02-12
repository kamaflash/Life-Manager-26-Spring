package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobPositionEntity;
import com.pet.businessdomain.jobservice.entities.enumjobs.JobCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobPositionRepository extends JpaRepository<JobPositionEntity, Long> {

    // Buscar posiciones por empresa
    List<JobPositionEntity> findByCompanyId(Long companyId);

    // Posiciones activas por categoría
    List<JobPositionEntity> findByCategoryAndActiveTrue(JobCategory category);
    @Query("""
    SELECT p
    FROM JobPositionEntity p
    JOIN FETCH p.company
    WHERE p.category = :category
""")
    Page<JobPositionEntity> findByCategoryAndActiveTrue(
            @Param("category") JobCategory category,
            Pageable pageable
    );


    List<JobPositionEntity> findByIdIn(List<Long> ids);



}
