package com.pet.businessdomain.jobservice.controller;

import com.pet.businessdomain.jobservice.services.JobApplicationService;
import com.pet.businessdomain.shareddto.dto.JobApplicationDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationRequestDTO;
import com.pet.businessdomain.shareddto.dto.JobApplicationResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/job-applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    // ===== CRUD BÁSICO =====

    /**
     * Postula a un personaje a una vacante de trabajo
     *
     * @param request Objeto con characterId y vacancyId
     * @return JobApplicationResultDTO con el resultado de la postulación y match score
     *
     * @example POST /api/job-applications/apply
     * @example Body: { "characterId": 789, "vacancyId": 456 }
     */
    @PostMapping("/apply")
    public ResponseEntity<JobApplicationResultDTO> apply(@RequestBody JobApplicationRequestDTO request) {
        log.info("POST /api/job-applications/apply - Character {} applying to vacancy {}",
                request.getCharacterId(), request.getVacancyId());
        return ResponseEntity.ok(jobApplicationService.apply(request));
    }

    /**
     * Obtiene una postulación por su ID
     *
     * @param id ID de la postulación
     * @return JobApplicationDTO con los datos de la postulación
     *
     * @example GET /api/job-applications/123
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> getById(@PathVariable(name = "id") Long id) {
        log.info("GET /api/job-applications/{} - Get application", id);
        return ResponseEntity.ok(jobApplicationService.getById(id));
    }

    /**
     * Retira una postulación (solo si está en estado PENDING)
     *
     * @param id ID de la postulación a retirar
     * @return 204 No Content si la operación es exitosa
     *
     * @example PATCH /api/job-applications/123/withdraw
     */
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/job-applications/{}/withdraw - Withdraw application", id);
        jobApplicationService.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    // ===== LISTADOS =====

    /**
     * Obtiene todas las postulaciones de un personaje
     *
     * @param characterId ID del personaje
     * @return Lista de JobApplicationDTO con las postulaciones del personaje
     *
     * @example GET /api/job-applications/character/789
     */
    @GetMapping("/character/{characterId}")
    public ResponseEntity<List<JobApplicationDTO>> getByCharacter(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/job-applications/character/{} - Get applications by character", characterId);
        return ResponseEntity.ok(jobApplicationService.getByCharacter(characterId));
    }

    /**
     * Obtiene todas las postulaciones de una vacante
     *
     * @param vacancyId ID de la vacante
     * @return Lista de JobApplicationDTO con las postulaciones de la vacante
     *
     * @example GET /api/job-applications/vacancy/456
     */
    @GetMapping("/vacancy/{vacancyId}")
    public ResponseEntity<List<JobApplicationDTO>> getByVacancy(@PathVariable(name = "vacancyId") Long vacancyId) {
        log.info("GET /api/job-applications/vacancy/{} - Get applications by vacancy", vacancyId);
        return ResponseEntity.ok(jobApplicationService.getByVacancy(vacancyId));
    }

    /**
     * Obtiene postulaciones por estado (PENDING, REVIEWING, INTERVIEW_SCHEDULED, etc.)
     *
     * @param status Estado de la postulación
     * @return Lista de JobApplicationDTO con las postulaciones en ese estado
     *
     * @example GET /api/job-applications/status/PENDING
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<JobApplicationDTO>> getByStatus(@PathVariable(name = "status") String status) {
        log.info("GET /api/job-applications/status/{} - Get applications by status", status);
        return ResponseEntity.ok(jobApplicationService.getByStatus(status));
    }

    /**
     * Obtiene todas las postulaciones pendientes de revisión
     *
     * @return Lista de JobApplicationDTO con postulaciones en estado PENDING
     *
     * @example GET /api/job-applications/pending-review
     */
    @GetMapping("/pending-review")
    public ResponseEntity<List<JobApplicationDTO>> getPendingReview() {
        log.info("GET /api/job-applications/pending-review - Get pending review applications");
        return ResponseEntity.ok(jobApplicationService.getPendingReview());
    }

    /**
     * Obtiene las postulaciones recientes de un personaje
     *
     * @param characterId ID del personaje
     * @return Lista de JobApplicationDTO con las últimas postulaciones (ordenadas por fecha descendente)
     *
     * @example GET /api/job-applications/character/789/recent
     */
    @GetMapping("/character/{characterId}/recent")
    public ResponseEntity<List<JobApplicationDTO>> getRecentByCharacter(@PathVariable(name = "characterId") Long characterId) {
        log.info("GET /api/job-applications/character/{}/recent - Get recent applications", characterId);
        return ResponseEntity.ok(jobApplicationService.getRecentByCharacter(characterId));
    }

    // ===== PROCESO DE SELECCIÓN =====

    /**
     * Marca una postulación como en revisión
     *
     * @param id ID de la postulación
     * @return JobApplicationDTO con el estado actualizado
     *
     * @example PATCH /api/job-applications/123/review
     */
    @PatchMapping("/{id}/review")
    public ResponseEntity<JobApplicationDTO> reviewApplication(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/job-applications/{}/review - Review application", id);
        return ResponseEntity.ok(jobApplicationService.reviewApplication(id));
    }

    /**
     * Programa una entrevista para el candidato
     *
     * @param id ID de la postulación
     * @param interviewDate Fecha y hora de la entrevista
     * @return JobApplicationDTO con los datos actualizados
     *
     * @example PATCH /api/job-applications/123/schedule-interview?interviewDate=2024-12-25T10:00:00
     */
    @PatchMapping("/{id}/schedule-interview")
    public ResponseEntity<JobApplicationDTO> scheduleInterview(
            @PathVariable(name = "id") Long id,
            @RequestParam LocalDateTime interviewDate) {
        log.info("PATCH /api/job-applications/{}/schedule-interview - Schedule interview at {}", id, interviewDate);
        return ResponseEntity.ok(jobApplicationService.scheduleInterview(id, interviewDate));
    }

    /**
     * Actualiza el resultado de una entrevista
     *
     * @param id ID de la postulación
     * @param result Resultado de la entrevista (PASSED, FAILED)
     * @param notes Notas adicionales sobre la entrevista (opcional)
     * @return JobApplicationDTO con los datos actualizados
     *
     * @example PATCH /api/job-applications/123/interview-result?result=PASSED&notes=Muy buen candidato
     */
    @PatchMapping("/{id}/interview-result")
    public ResponseEntity<JobApplicationDTO> updateInterviewResult(
            @PathVariable(name = "id") Long id,
            @RequestParam String result,
            @RequestParam(required = false) String notes) {
        log.info("PATCH /api/job-applications/{}/interview-result - Update interview result: {}", id, result);
        return ResponseEntity.ok(jobApplicationService.updateInterviewResult(id, result, notes));
    }

    /**
     * Realiza una oferta laboral al candidato
     *
     * @param id ID de la postulación
     * @param salary Salario ofrecido
     * @return JobApplicationDTO con los datos actualizados
     *
     * @example PATCH /api/job-applications/123/make-offer?salary=50000
     */
    @PatchMapping("/{id}/make-offer")
    public ResponseEntity<JobApplicationDTO> makeOffer(
            @PathVariable(name = "id") Long id,
            @RequestParam BigDecimal salary) {
        log.info("PATCH /api/job-applications/{}/make-offer - Make offer with salary {}", id, salary);
        return ResponseEntity.ok(jobApplicationService.makeOffer(id, salary));
    }

    /**
     * Contrata al candidato (finaliza el proceso exitosamente)
     *
     * @param id ID de la postulación
     * @return JobApplicationDTO con los datos actualizados
     *
     * @example PATCH /api/job-applications/123/hire
     */
    @PatchMapping("/{id}/hire")
    public ResponseEntity<JobApplicationDTO> hire(@PathVariable(name = "id") Long id) {
        log.info("PATCH /api/job-applications/{}/hire - Hire candidate", id);
        return ResponseEntity.ok(jobApplicationService.hire(id));
    }

    /**
     * Rechaza la postulación del candidato
     *
     * @param id ID de la postulación
     * @param reason Razón del rechazo (opcional)
     * @return JobApplicationDTO con los datos actualizados
     *
     * @example PATCH /api/job-applications/123/reject?reason=No+cumple+requisitos
     */
    @PatchMapping("/{id}/reject")
    public ResponseEntity<JobApplicationDTO> reject(
            @PathVariable(name = "id") Long id,
            @RequestParam(required = false) String reason) {
        log.info("PATCH /api/job-applications/{}/reject - Reject application: {}", id, reason);
        return ResponseEntity.ok(jobApplicationService.reject(id, reason));
    }

    // ===== VERIFICACIONES =====

    /**
     * Verifica si un personaje ya ha postulado a una vacante específica
     *
     * @param characterId ID del personaje
     * @param vacancyId ID de la vacante
     * @return true si ya postuló, false en caso contrario
     *
     * @example GET /api/job-applications/check?characterId=789&vacancyId=456
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasApplied(
            @RequestParam Long characterId,
            @RequestParam Long vacancyId) {
        log.info("GET /api/job-applications/check - Check if character {} applied to vacancy {}", characterId, vacancyId);
        return ResponseEntity.ok(jobApplicationService.hasApplied(characterId, vacancyId));
    }

    /**
     * Calcula el match score entre un personaje y una vacante
     *
     * @param characterId ID del personaje
     * @param vacancyId ID de la vacante
     * @return Puntaje de compatibilidad (0-100)
     *
     * @example GET /api/job-applications/789/456/match-score
     */
    @GetMapping("/{characterId}/{vacancyId}/match-score")
    public ResponseEntity<Integer> getMatchScore(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "vacancyId") Long vacancyId) {
        log.info("GET /api/job-applications/{}/{}/match-score - Get match score", characterId, vacancyId);
        return ResponseEntity.ok(jobApplicationService.getMatchScore(characterId, vacancyId));
    }

    // ===== ESTADÍSTICAS =====

    /**
     * Cuenta las postulaciones de un personaje por estado
     *
     * @param characterId ID del personaje
     * @param status Estado de la postulación
     * @return Número de postulaciones en ese estado
     *
     * @example GET /api/job-applications/character/789/status/PENDING/count
     */
    @GetMapping("/character/{characterId}/status/{status}/count")
    public ResponseEntity<Long> countByCharacterAndStatus(
            @PathVariable(name = "characterId") Long characterId,
            @PathVariable(name = "status") String status) {
        log.info("GET /api/job-applications/character/{}/status/{}/count - Count by character and status", characterId, status);
        return ResponseEntity.ok(jobApplicationService.countByCharacterAndStatus(characterId, status));
    }

    /**
     * Cuenta el total de postulaciones de una vacante
     *
     * @param vacancyId ID de la vacante
     * @return Número total de postulaciones
     *
     * @example GET /api/job-applications/vacancy/456/count
     */
    @GetMapping("/vacancy/{vacancyId}/count")
    public ResponseEntity<Integer> countByVacancy(@PathVariable(name = "vacancyId") Long vacancyId) {
        log.info("GET /api/job-applications/vacancy/{}/count - Count by vacancy", vacancyId);
        return ResponseEntity.ok(jobApplicationService.countByVacancy(vacancyId));
    }
}