package com.pet.businessdomain.jobservice.repository;

import com.pet.businessdomain.jobservice.entities.WorkRelationshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRelationshipRepository extends JpaRepository<WorkRelationshipEntity, Long> {

    // ===== BÚSQUEDAS BÁSICAS =====

    // Relaciones de un trabajo
    List<WorkRelationshipEntity> findByCharacterJobId(Long characterJobId);

    // Relaciones por tipo (genérico)
    List<WorkRelationshipEntity> findByCharacterJobIdAndRelationshipType(Long characterJobId, String relationshipType);

    // Relaciones con alta afinidad
    List<WorkRelationshipEntity> findByCharacterJobIdAndAffinityGreaterThan(Long characterJobId, Integer affinity);

    // Relaciones con baja afinidad (conflictos)
    List<WorkRelationshipEntity> findByCharacterJobIdAndAffinityLessThan(Long characterJobId, Integer affinity);

    // ===== BÚSQUEDAS ESPECÍFICAS POR TIPO =====

    // Obtener el jefe del personaje (un solo resultado)
    @Query("SELECT wr FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'BOSS'")
    Optional<WorkRelationshipEntity> findBossByJobId(@Param("jobId") Long jobId);

    // Obtener el mentor del personaje (un solo resultado)
    @Query("SELECT wr FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'MENTOR'")
    Optional<WorkRelationshipEntity> findMentorByJobId(@Param("jobId") Long jobId);

    // Obtener compañeros de trabajo
    @Query("SELECT wr FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'COLLEAGUE'")
    List<WorkRelationshipEntity> findColleaguesByJobId(@Param("jobId") Long jobId);

    // Obtener rivales
    @Query("SELECT wr FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'RIVAL'")
    List<WorkRelationshipEntity> findRivalsByJobId(@Param("jobId") Long jobId);

    // ===== VERIFICACIONES =====

    // Verificar si ya existe relación con ese personaje
    boolean existsByCharacterJobIdAndRelatedCharacterId(Long characterJobId, Long relatedCharacterId);

    // Verificar si el personaje ya tiene jefe
    @Query("SELECT COUNT(wr) > 0 FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'BOSS'")
    boolean hasBoss(@Param("jobId") Long jobId);

    // Verificar si el personaje ya tiene mentor
    @Query("SELECT COUNT(wr) > 0 FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'MENTOR'")
    boolean hasMentor(@Param("jobId") Long jobId);

    // ===== ACTUALIZACIONES =====

    // Actualizar afinidad
    @Modifying
    @Transactional
    @Query("UPDATE WorkRelationshipEntity wr SET wr.affinity = wr.affinity + :change WHERE wr.id = :id")
    void updateAffinity(@Param("id") Long id, @Param("change") Integer change);

    // Eliminar todas las relaciones de un trabajo
    void deleteByCharacterJobId(Long characterJobId);

    // ===== CONTADORES =====

    // Contar relaciones por tipo
    @Query("SELECT COUNT(wr) FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = :type")
    Long countByJobIdAndType(@Param("jobId") Long jobId, @Param("type") String type);

    // Contar compañeros con alta afinidad
    @Query("SELECT COUNT(wr) FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'COLLEAGUE' AND wr.affinity > :minAffinity")
    Long countHighAffinityColleagues(@Param("jobId") Long jobId, @Param("minAffinity") Integer minAffinity);

    // Contar rivales con baja afinidad
    @Query("SELECT COUNT(wr) FROM WorkRelationshipEntity wr " +
            "WHERE wr.characterJobId = :jobId AND wr.relationshipType = 'RIVAL' AND wr.affinity < :maxAffinity")
    Long countLowAffinityRivals(@Param("jobId") Long jobId, @Param("maxAffinity") Integer maxAffinity);
}