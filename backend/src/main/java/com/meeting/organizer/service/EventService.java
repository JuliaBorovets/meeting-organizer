package com.meeting.organizer.service;

import com.meeting.organizer.model.Event;
import com.meeting.organizer.web.dto.v1.event.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventService extends CRUDService<Event> {

    EventDto createEvent(EventCreateDto eventCreateDto);

    EventDto updateEvent(EventUpdateDto eventUpdateDto);

    EventDto  uploadEventImage(Long eventId, MultipartFile image);

    void deleteEvent(Long id);

    EventResponse findAllByLibraryId(Long userId, Long libraryId, Long streamId, String eventName, Pageable pageable);

    EventResponse findAll(Long userId, String eventName, Pageable pageable);

    List<Event> findAllByUser(Long userId);

    EventResponse findAllByUserPageable(Long userId, String eventName, Pageable pageable);

    EventResponse findAllByNotLibraryId(Long userId, Long libraryId, String eventName, Pageable pageable);

    EventDto findEventById(Long eventId);

    List<EventDto> findAllByNameAndStreamNotContaining(Long userId, Long libraryId, Long streamId, String eventName, Pageable pageable);

    EventDto addEventToFavorites(Long eventId, Long userId);

    EventDto removeEventFromFavorites(Long eventId, Long userId);

    EventResponse getUserFavoriteEventsPaginated(Long userId, String eventName, Pageable pageable);

    EventResponse getEventsGivenAccessListByUser(Long userId, String eventName, Pageable pageable);

    EventDto addAccessToEventByUserEmail(AddEventAccessDto addEventAccessDto);

    EventDto removeAccessFromEventByUserEmail(List<String> emailList,  Long eventId);

    EventDto addAccessToEventByToken(AddEventAccessByTokenDto addEventAccessDto);

    EventDto addVisitorToEvent(Long eventId, Long userId);

    EventDto deleteVisitorFromEvent(Long eventId, Long userId);

    List<EventDto> findAllByNameAndLibraryNotContaining(Long userId, Long libraryId, String eventName, Pageable pageable);

    Boolean eventExists(Long id);

}
