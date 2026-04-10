package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.CompanyEntity;
import com.pet.businessdomain.shareddto.enumentities.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    // Búsqueda por nombre (parcial)
    List<CompanyEntity> findByNameContainingIgnoreCase(String name);

    // Empresas activas
    List<CompanyEntity> findByActiveTrue();

    // Empresas por categoría
    List<CompanyEntity> findByCategory(JobCategory category);

    // Empresas por ubicación
    List<CompanyEntity> findByLocationContainingIgnoreCase(String location);

    // Empresas con alta reputación (> 70)
//    List<CompanyEntity> findByReputationGreaterThan(Integer reputation);

    // Empresas que aceptan teletrabajo
    List<CompanyEntity> findByRemoteFriendlyTrue();

    // Empresas con prácticas disponibles
    List<CompanyEntity> findByInternshipAvailableTrue();

    // Búsqueda combinada
    @Query("SELECT c FROM CompanyEntity c WHERE " +
            "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:category IS NULL OR c.category = :category) AND " +
            "(:location IS NULL OR LOWER(c.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:active IS NULL OR c.active = :active)")
    List<CompanyEntity> searchCompanies(@Param("name") String name,
                                        @Param("category") JobCategory category,
                                        @Param("location") String location,
                                        @Param("active") Boolean active);

    // Contar vacantes activas por empresa
    @Query("SELECT c.id, COUNT(v) FROM CompanyEntity c " +
            "LEFT JOIN c.positions p " +
            "LEFT JOIN p.vacancies v " +
            "WHERE v.active = true AND v.availableSlots > 0 " +
            "GROUP BY c.id")
    List<Object[]> countActiveVacanciesByCompany();

    // Top empresas con más vacantes
    @Query("SELECT c, COUNT(v) as vacanciesCount FROM CompanyEntity c " +
            "LEFT JOIN c.positions p " +
            "LEFT JOIN p.vacancies v " +
            "WHERE v.active = true AND v.availableSlots > 0 " +
            "GROUP BY c.id " +
            "ORDER BY vacanciesCount DESC")
    List<CompanyEntity> findTopCompaniesByVacancies();
}