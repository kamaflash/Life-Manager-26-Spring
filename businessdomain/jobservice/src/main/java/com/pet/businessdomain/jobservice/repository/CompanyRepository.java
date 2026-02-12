package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    // Buscar empresas por categoría
    List<CompanyEntity> findByCategory(String category);

}
