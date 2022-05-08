package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.event.EventCreateDto;
import com.meeting.organizer.web.dto.v1.event.EventDto;
import com.meeting.organizer.web.dto.v1.event.EventResponse;
import com.meeting.organizer.web.dto.v1.event.EventUpdateDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import com.meeting.organizer.web.dto.v1.library.LibraryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventCreateDto eventCreateDto);

    EventDto updateEvent(EventUpdateDto eventUpdateDto);

    void deleteEvent(Long id);

    EventResponse findAllByLibraryId(Long userId, Long libraryId, Long streamId, Pageable pageable);

    EventResponse findAllByNotLibraryId(Long userId, Long libraryId, Pageable pageable);

    EventDto findEventById(Long eventId);

    List<EventDto> findAllByNameAndStreamNotContaining(Long userId, Long libraryId, Long streamId, String name, Pageable pageable);

    EventDto addEventToFavorites(Long eventId, Long userId);

    EventDto removeEventFromFavorites(Long eventId, Long userId);

    EventResponse getUserFavoriteEventsPaginated(Long userId, Pageable pageable);
}
