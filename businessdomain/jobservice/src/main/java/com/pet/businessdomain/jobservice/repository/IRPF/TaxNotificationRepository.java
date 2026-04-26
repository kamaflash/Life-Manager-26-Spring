package com.pet.businessdomain.jobservice.repository.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxNotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaxNotificationRepository extends JpaRepository<TaxNotificationEntity, Long> {

    // Por personaje
    List<TaxNotificationEntity> findByCharacterIdOrderByCreatedAtDesc(Long characterId);

    Page<TaxNotificationEntity> findByCharacterId(Long characterId, Pageable pageable);

    // No leídas (PENDING o SENT)
    @Query("SELECT n FROM TaxNotificationEntity n WHERE n.characterId = :characterId AND n.status IN ('PENDING', 'SENT') ORDER BY n.createdAt DESC")
    List<TaxNotificationEntity> findUnreadByCharacter(@Param("characterId") Long characterId);

    // Por tipo
    List<TaxNotificationEntity> findByNotificationTypeAndStatus(String notificationType, String status);

    // Notificaciones pendientes de envío
    @Query("SELECT n FROM TaxNotificationEntity n WHERE n.status = 'PENDING' AND n.scheduledAt <= :now")
    List<TaxNotificationEntity> findPendingToSend(@Param("now") LocalDateTime now);

    // Por declaración
    List<TaxNotificationEntity> findByTaxFilingId(Long taxFilingId);

    // Actualizaciones masivas
    @Modifying
    @Transactional
    @Query("UPDATE TaxNotificationEntity n SET n.status = 'SENT', n.sentAt = :sentAt WHERE n.status = 'PENDING' AND n.id IN :ids")
    int markAsSent(@Param("ids") List<Long> ids, @Param("sentAt") LocalDateTime sentAt);

    @Modifying
    @Transactional
    @Query("UPDATE TaxNotificationEntity n SET n.status = 'READ', n.readAt = :readAt WHERE n.id = :id AND n.status IN ('SENT', 'PENDING')")
    int markAsRead(@Param("id") Long id, @Param("readAt") LocalDateTime readAt);

    // Limpieza de notificaciones antiguas
    @Modifying
    @Transactional
    void deleteByCreatedAtBefore(LocalDateTime date);

    // Contadores
    long countByCharacterIdAndStatusIn(Long characterId, List<String> statuses);

    @Query("SELECT COUNT(n) FROM TaxNotificationEntity n WHERE n.characterId = :characterId AND n.status IN ('PENDING', 'SENT')")
    long countUnreadByCharacter(@Param("characterId") Long characterId);
}
