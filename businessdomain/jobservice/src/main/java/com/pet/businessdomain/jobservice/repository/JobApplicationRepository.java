package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.JobApplicationEntity;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, Long> {

    // Aplicaciones de un personaje
    List<JobApplicationEntity> findByCharacterId(Long characterId);

    // Aplicaciones de un personaje a una vacante específica
    List<JobApplicationEntity> findByCharacterIdAndVacancyId(Long characterId, Long vacancyId);

    // Aplicaciones por vacante
    List<JobApplicationEntity> findByVacancyId(Long vacancyId);

    // Aplicaciones por estado
    List<JobApplicationEntity> findByStatus(String status);

    // Aplicaciones por etapa del proceso
    List<JobApplicationEntity> findByStage(String stage);

    List<JobApplicationEntity> findByCharacterIdAndStatus(Long characterId, EnumAll.ApplicationStatus status);
    List<JobApplicationEntity> findByCharacterIdAndStatusAndInterviewDateBetween(
            Long characterId,
            EnumAll.ApplicationStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    // IDs de vacantes a las que aplicó un personaje
    @Query("SELECT a.vacancy.id FROM JobApplicationEntity a WHERE a.characterId = :characterId")
    List<Long> findVacancyIdsByCharacterId(@Param("characterId") Long characterId);

    // Aplicaciones pendientes de revisión
    List<JobApplicationEntity> findByStatusIn(List<String> statuses);

    // Aplicaciones con entrevista programada
    List<JobApplicationEntity> findByInterviewDateAfter(LocalDateTime date);

    // Verificar si ya aplicó a una vacante
    boolean existsByCharacterIdAndVacancyId(Long characterId, Long vacancyId);

    // Contar aplicaciones por vacante
    @Query("SELECT COUNT(a) FROM JobApplicationEntity a WHERE a.vacancy.id = :vacancyId")
    Integer countByVacancyId(@Param("vacancyId") Long vacancyId);

    // Contar aplicaciones por personaje y estado
    Long countByCharacterIdAndStatus(Long characterId, String status);

    // Aplicaciones recientes de un personaje
    List<JobApplicationEntity> findTop10ByCharacterIdOrderByAppliedAtDesc(Long characterId);

    // Actualizar estado de aplicación
    @Modifying
    @Transactional
    @Query("UPDATE JobApplicationEntity a SET a.status = :status, a.stage = :stage WHERE a.id = :id")
    void updateStatus(@Param("id") Long id,
                      @Param("status") String status,
                      @Param("stage") String stage);

    // Programar entrevista
    @Modifying
    @Transactional
    @Query("UPDATE JobApplicationEntity a SET a.interviewDate = :interviewDate, " +
            "a.status = 'INTERVIEW_SCHEDULED', a.stage = :stage WHERE a.id = :id")
    void scheduleInterview(@Param("id") Long id,
                           @Param("interviewDate") LocalDateTime interviewDate,
                           @Param("stage") String stage);

    // Aplicaciones que se pueden procesar (sin entrevista o pasada la fecha)
    @Query("SELECT a FROM JobApplicationEntity a " +
            "WHERE a.status = 'INTERVIEW_SCHEDULED' AND a.interviewDate < CURRENT_TIMESTAMP")
    List<JobApplicationEntity> findPendingInterviewResults();

    @Query("SELECT DISTINCT a FROM JobApplicationEntity a " +
            "LEFT JOIN FETCH a.vacancy v " +
            "LEFT JOIN FETCH v.position p " +
            "LEFT JOIN FETCH p.company c " +
            "WHERE a.id = :id")
    Optional<JobApplicationEntity> findByIdWithAllRelations(@Param("id") Long id);
}