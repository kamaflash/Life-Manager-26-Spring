package com.pet.businessdomain.eventservice.services;

import com.pet.businessdomain.shareddto.dto.CharacterEventRecordResponseDto;
import com.pet.businessdomain.shareddto.dto.EventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    // Gestión de Eventos
    EventResponseDto createEvent(EventResponseDto eventDto);
    EventResponseDto updateEvent(Long id, EventResponseDto eventDto);
    EventResponseDto getEventById(Long id);
    EventResponseDto getEventByCode(String code);
    List<EventResponseDto> getAllEvents();
    Page<EventResponseDto> getAllEventsPaginated(Pageable pageable);
    List<EventResponseDto> getActiveEvents();
    List<EventResponseDto> getEventsByCity(String city);
    List<EventResponseDto> getEventsByType(String type);
    void deleteEvent(Long id);
    void cancelEvent(Long id);

    // Participación en Eventos
    CharacterEventRecordResponseDto registerCharacterToEvent(Long characterId, Long eventId);
    CharacterEventRecordResponseDto updateParticipationStatus(Long recordId, String status);
    CharacterEventRecordResponseDto attendEvent(Long recordId, String outcome, String outcomeNote);
    CharacterEventRecordResponseDto claimEventRewards(Long recordId);
    List<CharacterEventRecordResponseDto> getCharacterEventRecords(Long characterId);
    CharacterEventRecordResponseDto getCharacterEventRecord(Long characterId, Long eventId);

    // Eventos automáticos y disponibles
    List<EventResponseDto> getAutoTriggerEventsForCharacter(Long characterId);
    void processAutoTriggerEvents(Long characterId);
    List<EventResponseDto> getAvailableEventsForCharacter(Long characterId);
    List<EventResponseDto> createEventBatch(List<EventResponseDto> dtos);
}