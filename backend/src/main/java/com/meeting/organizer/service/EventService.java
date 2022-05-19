package com.meeting.organizer.service;

import com.meeting.organizer.model.Event;
import com.meeting.organizer.web.dto.v1.event.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventCreateDto eventCreateDto);

    EventDto updateEvent(EventUpdateDto eventUpdateDto);

    void deleteEvent(Long id);

    EventResponse findAllByLibraryId(Long userId, Long libraryId, Long streamId, Pageable pageable);

    EventResponse findAll(Long userId, Pageable pageable);

    List<Event> findAllByUser(Long userId);

    EventResponse findAllByUserPageable(Long userId, Pageable pageable);

    EventResponse findAllByNotLibraryId(Long userId, Long libraryId, Pageable pageable);

    EventDto findEventById(Long eventId);

    List<EventDto> findAllByNameAndStreamNotContaining(Long userId, Long libraryId, Long streamId, String name, Pageable pageable);

    EventDto addEventToFavorites(Long eventId, Long userId);

    EventDto removeEventFromFavorites(Long eventId, Long userId);

    EventResponse getUserFavoriteEventsPaginated(Long userId, Pageable pageable);

    EventResponse getEventsGivenAccessListByUser(Long userId, Pageable pageable);

    EventDto addAccessToEventByUserEmail(AddEventAccessDto addEventAccessDto);

    EventDto removeAccessFromEventByUserEmail(List<String> emailList,  Long eventId);

    EventDto addAccessToEventByToken(AddEventAccessByTokenDto addEventAccessDto);

    EventDto addVisitorToEvent(Long eventId, Long userId);

    EventDto deleteVisitorFromEvent(Long eventId, Long userId);
}
