package com.pet.businessdomain.eventservice.services;

import com.pet.businessdomain.eventservice.entities.CharacterEventRecord;
import com.pet.businessdomain.eventservice.entities.EventEntity;
import com.pet.businessdomain.eventservice.mapper.CharacterEventRecordMapper;
import com.pet.businessdomain.eventservice.mapper.EventMapper;
import com.pet.businessdomain.eventservice.repository.CharacterEventRecordRepository;
import com.pet.businessdomain.eventservice.repository.EventRepository;
import com.pet.businessdomain.eventservice.services.EventService;
import com.pet.businessdomain.eventservice.transactions.BusinessTransactions;
import com.pet.businessdomain.shareddto.dto.CharacterDto;
import com.pet.businessdomain.shareddto.dto.CharacterEventRecordResponseDto;
import com.pet.businessdomain.shareddto.dto.EventResponseDto;
import com.pet.businessdomain.shareddto.enumentities.EnumAll;
import com.pet.businessdomain.eventservice.exceptions.BusinessValidationException;
import com.pet.businessdomain.eventservice.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CharacterEventRecordRepository recordRepository;
    private final BusinessTransactions businessTransactions;
    private final EventMapper eventMapper;
    private final CharacterEventRecordMapper recordMapper;

    @Override
    public EventResponseDto createEvent(EventResponseDto eventDto) {
        log.info("Creating event with code: {}", eventDto.getCode());

        if (eventRepository.existsByCode(eventDto.getCode())) {
            throw new BusinessValidationException("Event code already exists: " + eventDto.getCode());
        }

        validateEventDates(eventDto.getStartDate(), eventDto.getEndDate());

        EventEntity event = eventMapper.toEntity(eventDto);
        event.setStatus(EnumAll.EventStatus.SCHEDULED);
        event.setCreatedAt(LocalDateTime.now());

        EventEntity savedEvent = eventRepository.save(event);
        log.info("Event created successfully with ID: {}", savedEvent.getId());

        return eventMapper.toDto(savedEvent);
    }

    @Override
    public EventResponseDto updateEvent(Long id, EventResponseDto eventDto) {
        log.info("Updating event with ID: {}", id);

        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        validateEventDates(eventDto.getStartDate(), eventDto.getEndDate());

        EventEntity updatedEntity = eventMapper.toEntity(eventDto);
        updatedEntity.setId(id);
        updatedEntity.setCreatedAt(event.getCreatedAt());

        EventEntity savedEvent = eventRepository.save(updatedEntity);
        log.info("Event updated successfully with ID: {}", id);

        return eventMapper.toDto(savedEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventById(Long id) {
        log.debug("Fetching event with ID: {}", id);

        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        return eventMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventByCode(String code) {
        log.debug("Fetching event with code: {}", code);

        EventEntity event = eventRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with code: " + code));

        return eventMapper.toDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAllEvents() {
        log.debug("Fetching all events");

        return eventRepository.findAll().stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> getAllEventsPaginated(Pageable pageable) {
        log.debug("Fetching all events with pagination");

        return eventRepository.findAll(pageable)
                .map(eventMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getActiveEvents() {
        log.debug("Fetching active events");

        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        EnumAll.EventStatus.IN_PROGRESS, now, now).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getEventsByCity(String city) {
        log.debug("Fetching events for city: {}", city);

        return eventRepository.findByCity(city).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getEventsByType(String type) {
        log.debug("Fetching events by type: {}", type);

        return eventRepository.findByType(EnumAll.EventType.valueOf(type)).stream()
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEvent(Long id) {
        log.info("Deleting event with ID: {}", id);

        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }

        eventRepository.deleteById(id);
        log.info("Event deleted successfully with ID: {}", id);
    }

    @Override
    public void cancelEvent(Long id) {
        log.info("Cancelling event with ID: {}", id);

        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        event.setStatus(EnumAll.EventStatus.CANCELLED);
        eventRepository.save(event);

        log.info("Event cancelled successfully with ID: {}", id);
    }

    @Override
    public CharacterEventRecordResponseDto registerCharacterToEvent(Long characterId, Long eventId) {
        log.info("Registering character {} to event {}", characterId, eventId);

        CharacterDto character = businessTransactions.getPerson(characterId);
        if (character == null) {
            throw new ResourceNotFoundException("Character not found with ID: " + characterId);
        }

        EventEntity event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + eventId));

        validateEventRegistration(character, event);

        CharacterEventRecord record = new CharacterEventRecord();
        record.setCharacterId(characterId);
        record.setEventId(eventId);
        record.setStatus(EnumAll.EventParticipationStatus.REGISTERED);
        record.setRegisteredAt(LocalDateTime.now());
        record.setRewardsClaimed(false);

        CharacterEventRecord savedRecord = recordRepository.save(record);
        log.info("Character {} registered successfully to event {}", characterId, eventId);

        return recordMapper.toDto(savedRecord);
    }

    @Override
    public CharacterEventRecordResponseDto updateParticipationStatus(Long recordId, String status) {
        log.info("Updating participation status for record {} to {}", recordId, status);

        CharacterEventRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found with ID: " + recordId));

        record.setStatus(EnumAll.EventParticipationStatus.valueOf(status));

        if ("ATTENDED".equals(status)) {
            record.setAttendedAt(LocalDateTime.now());
        }

        CharacterEventRecord updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    @Override
    public CharacterEventRecordResponseDto attendEvent(Long recordId, String outcome, String outcomeNote) {
        log.info("Marking attendance for record {} with outcome {}", recordId, outcome);

        CharacterEventRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found with ID: " + recordId));

        record.setStatus(EnumAll.EventParticipationStatus.ATTENDED);
        record.setAttendedAt(LocalDateTime.now());

        if (outcome != null) {
            record.setOutcome(EnumAll.EventOutcome.valueOf(outcome));
        }
        if (outcomeNote != null) {
            record.setOutcomeNote(outcomeNote);
        }

        CharacterEventRecord updatedRecord = recordRepository.save(record);
        return recordMapper.toDto(updatedRecord);
    }

    @Override
    public CharacterEventRecordResponseDto claimEventRewards(Long recordId) {
        log.info("Claiming rewards for event record {}", recordId);

        CharacterEventRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found with ID: " + recordId));

        if (record.isRewardsClaimed()) {
            throw new BusinessValidationException("Rewards already claimed for this event");
        }

        if (record.getStatus() != EnumAll.EventParticipationStatus.ATTENDED) {
            throw new BusinessValidationException("Must attend event before claiming rewards");
        }

        EventEntity event = eventRepository.findById(record.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        processEventRewards(record.getCharacterId(), event);

        record.setRewardsClaimed(true);
        CharacterEventRecord updatedRecord = recordRepository.save(record);

        log.info("Rewards claimed successfully for record {}", recordId);
        return recordMapper.toDto(updatedRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterEventRecordResponseDto> getCharacterEventRecords(Long characterId) {
        log.debug("Fetching event records for character {}", characterId);

        return recordRepository.findByCharacterId(characterId).stream()
                .map(recordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterEventRecordResponseDto getCharacterEventRecord(Long characterId, Long eventId) {
        log.debug("Fetching event record for character {} and event {}", characterId, eventId);

        CharacterEventRecord record = recordRepository.findByCharacterIdAndEventId(characterId, eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event record not found"));

        return recordMapper.toDto(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAutoTriggerEventsForCharacter(Long characterId) {
        log.debug("Fetching auto-trigger events for character {}", characterId);

        CharacterDto character = businessTransactions.getPerson(characterId);
        if (character == null) {
            throw new ResourceNotFoundException("Character not found with ID: " + characterId);
        }

        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByAutoTriggerTrue().stream()
//                .filter(event -> event.getStatus() == EnumAll.EventStatus.IN_PROGRESS)
//                .filter(event -> isEventAvailableForCharacter(event, character))
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void processAutoTriggerEvents(Long characterId) {
        log.info("Processing auto-trigger events for character {}", characterId);

        List<EventResponseDto> autoEvents = getAutoTriggerEventsForCharacter(characterId);

        for (EventResponseDto event : autoEvents) {
            try {
                if (!recordRepository.existsByCharacterIdAndEventId(characterId, event.getId())) {
                    registerCharacterToEvent(characterId, event.getId());
                }
            } catch (Exception e) {
                log.error("Failed to auto-register character {} to event {}: {}",
                        characterId, event.getId(), e.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventResponseDto> getAvailableEventsForCharacter(Long characterId) {
        log.debug("Fetching available events for character {}", characterId);

        CharacterDto character = businessTransactions.getPerson(characterId);
        if (character == null) {
            throw new ResourceNotFoundException("Character not found with ID: " + characterId);
        }

        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByStatus(EnumAll.EventStatus.IN_PROGRESS).stream()
                .filter(event -> isEventAvailableForCharacter(event, character))
                .filter(event -> !recordRepository.existsByCharacterIdAndEventId(characterId, event.getId()))
                .map(eventMapper::toDto)
                .collect(Collectors.toList());
    }

    // Métodos privados de utilidad
    private void validateEventDates(LocalDateTime startDate, LocalDateTime endDate) {
        // Si no se proporcionan fechas, no validamos (se usarán los valores por defecto de la entidad)
        if (startDate == null && endDate == null) {
            log.debug("No dates provided, using default values");
            return;
        }

        // Si solo una fecha es null, asignamos valores por defecto
        LocalDateTime validStartDate = startDate != null ? startDate : LocalDateTime.now();
        LocalDateTime validEndDate = endDate != null ? endDate : validStartDate.plusDays(7);

        if (validStartDate.isAfter(validEndDate)) {
            throw new BusinessValidationException("Start date must be before end date");
        }
    }

    private void validateEventRegistration(CharacterDto character, EventEntity event) {
        if (event.getStatus() != EnumAll.EventStatus.IN_PROGRESS) {
            throw new BusinessValidationException("Event is not active");
        }

        if (event.getMaxParticipants() != null) {
            long currentCount = recordRepository.countByEventId(event.getId());
            if (currentCount >= event.getMaxParticipants()) {
                throw new BusinessValidationException("Event has reached maximum participants");
            }
        }

        if (recordRepository.existsByCharacterIdAndEventId(character.getId(), event.getId())) {
            throw new BusinessValidationException("Character already registered for this event");
        }

        if (event.getScope() == EnumAll.EventScope.CITY &&
                !event.getCity().equals(character.getCity())) {
            throw new BusinessValidationException("Character must be in " + event.getCity() + " to attend this event");
        }
    }

    private boolean isEventAvailableForCharacter(EventEntity event, CharacterDto character) {
        if (event.getScope() == EnumAll.EventScope.CITY &&
                !event.getCity().equals(character.getCity())) {
            return false;
        }

        return true;
    }
    @Override
    @Transactional
    public List<EventResponseDto> createEventBatch(List<EventResponseDto> dtos) {
        log.info("Processing batch of {} missions", dtos.size());

        List<EventEntity> entitiesToSave = new ArrayList<>();
        List<EventResponseDto> result = new ArrayList<>();

        for (EventResponseDto dto : dtos) {
            // Si ya existe, lo saltamos
            if (eventRepository.existsByCode(dto.getCode())) {
                log.warn("Mission {} already exists, skipping", dto.getCode());
                continue;
            }

            EventEntity mission = eventMapper.toEntity(dto);
            mission.setCreatedAt(LocalDateTime.now());
            entitiesToSave.add(mission);
        }

        if (!entitiesToSave.isEmpty()) {
            List<EventEntity> saved = eventRepository.saveAll(entitiesToSave);
            result = saved.stream()
                    .map(eventMapper::toDto)
                    .collect(Collectors.toList());
        }

        log.info("Successfully created {} missions", result.size());
        return result;
    }
    private void processEventRewards(Long characterId, EventEntity event) {
//        if (event.getRewards() != null) {
//            characterServiceClient.applyRewards(characterId, event.getRewards());
//        }
    }
}