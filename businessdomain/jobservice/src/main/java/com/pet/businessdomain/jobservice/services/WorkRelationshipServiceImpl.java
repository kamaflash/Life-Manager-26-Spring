package com.pet.businessdomain.jobservice.services;

import com.pet.businessdomain.jobservice.entities.WorkRelationshipEntity;
import com.pet.businessdomain.jobservice.mapper.WorkRelationshipMapper;
import com.pet.businessdomain.jobservice.repository.WorkRelationshipRepository;
import com.pet.businessdomain.shareddto.dto.WorkRelationshipDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkRelationshipServiceImpl implements WorkRelationshipService {

    private final WorkRelationshipRepository workRelationshipRepository;
    private final WorkRelationshipMapper workRelationshipMapper;

    @Override
    @Transactional
    public WorkRelationshipDTO create(WorkRelationshipDTO dto) {
        log.info("Creating work relationship: {}", dto.getRelationshipType());

        WorkRelationshipEntity entity = workRelationshipMapper.toEntity(dto);
        WorkRelationshipEntity saved = workRelationshipRepository.save(entity);
        return workRelationshipMapper.toDto(saved);
    }

    @Override
    @Transactional
    public WorkRelationshipDTO update(Long id, WorkRelationshipDTO dto) {
        log.info("Updating work relationship with id: {}", id);

        WorkRelationshipEntity entity = workRelationshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work relationship not found with id: " + id));

        workRelationshipMapper.updateEntity(dto, entity);
        WorkRelationshipEntity updated = workRelationshipRepository.save(entity);
        return workRelationshipMapper.toDto(updated);
    }

    @Override
    public WorkRelationshipDTO getById(Long id) {
        return workRelationshipRepository.findById(id)
                .map(workRelationshipMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Work relationship not found with id: " + id));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting work relationship with id: {}", id);
        workRelationshipRepository.deleteById(id);
    }

    @Override
    public List<WorkRelationshipDTO> getByCharacterJob(Long characterJobId) {
        return workRelationshipMapper.toDtoList(workRelationshipRepository.findByCharacterJobId(characterJobId));
    }

    @Override
    public List<WorkRelationshipDTO> getBoss(Long characterJobId) {
        // ✅ CORREGIDO: Convertir Optional<WorkRelationshipEntity> a List<WorkRelationshipDTO>
        return workRelationshipRepository.findBossByJobId(characterJobId)
                .map(workRelationshipMapper::toDto)
                .map(List::of)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<WorkRelationshipDTO> getMentor(Long characterJobId) {
        // ✅ CORREGIDO: Convertir Optional<WorkRelationshipEntity> a List<WorkRelationshipDTO>
        return workRelationshipRepository.findMentorByJobId(characterJobId)
                .map(workRelationshipMapper::toDto)
                .map(List::of)
                .orElse(Collections.emptyList());
    }

    @Override
    public List<WorkRelationshipDTO> getColleagues(Long characterJobId) {
        // ✅ CORREGIDO: Convertir List<WorkRelationshipEntity> a List<WorkRelationshipDTO>
        List<WorkRelationshipEntity> colleagues = workRelationshipRepository.findColleaguesByJobId(characterJobId);
        return workRelationshipMapper.toDtoList(colleagues);
    }

    @Override
    public List<WorkRelationshipDTO> getRivals(Long characterJobId) {
        // ✅ CORREGIDO: Convertir List<WorkRelationshipEntity> a List<WorkRelationshipDTO>
        List<WorkRelationshipEntity> rivals = workRelationshipRepository.findRivalsByJobId(characterJobId);
        return workRelationshipMapper.toDtoList(rivals);
    }

    @Override
    public List<WorkRelationshipDTO> getHighAffinity(Long characterJobId) {
        return workRelationshipMapper.toDtoList(
                workRelationshipRepository.findByCharacterJobIdAndAffinityGreaterThan(characterJobId, 50));
    }

    @Override
    public List<WorkRelationshipDTO> getLowAffinity(Long characterJobId) {
        return workRelationshipMapper.toDtoList(
                workRelationshipRepository.findByCharacterJobIdAndAffinityLessThan(characterJobId, 0));
    }

    @Override
    @Transactional
    public WorkRelationshipDTO improveAffinity(Long id, Integer amount) {
        log.info("Improving affinity for relationship {} by {}", id, amount);
        workRelationshipRepository.updateAffinity(id, amount);
        return getById(id);
    }

    @Override
    @Transactional
    public WorkRelationshipDTO worsenAffinity(Long id, Integer amount) {
        log.info("Worsening affinity for relationship {} by {}", id, amount);
        workRelationshipRepository.updateAffinity(id, -amount);
        return getById(id);
    }

    @Override
    public boolean hasBoss(Long characterJobId) {
        return workRelationshipRepository.hasBoss(characterJobId);
    }

    @Override
    public boolean hasMentor(Long characterJobId) {
        return workRelationshipRepository.hasMentor(characterJobId);
    }

    @Override
    public boolean existsRelationship(Long characterJobId, Long relatedCharacterId) {
        return workRelationshipRepository.existsByCharacterJobIdAndRelatedCharacterId(characterJobId, relatedCharacterId);
    }

    @Override
    @Transactional
    public void initializeRelationships(Long characterJobId, Long companyId) {
        log.info("Initializing relationships for job {} in company {}", characterJobId, companyId);

        // TODO: Implementar lógica para generar relaciones iniciales
        // - Asignar un jefe aleatorio
        // - Asignar un mentor (opcional)
        // - Generar algunos compañeros
        // - Posiblemente un rival
    }
}