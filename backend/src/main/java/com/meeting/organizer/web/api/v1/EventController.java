package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.EventService;
import com.meeting.organizer.service.FileStorageService;
import com.meeting.organizer.service.UserService;
import com.meeting.organizer.web.dto.v1.event.EventCreateDto;
import com.meeting.organizer.web.dto.v1.event.EventDto;
import com.meeting.organizer.web.dto.v1.event.EventResponse;
import com.meeting.organizer.web.dto.v1.event.EventUpdateDto;
import com.meeting.organizer.web.dto.v1.user.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(EventController.BASE_URL)
public class EventController {

    public static final String BASE_URL = "/api/v1/event";
    private final EventService eventService;
    private final UserService userService;

    @PostMapping
    public EventDto createEvent(@RequestBody EventCreateDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PatchMapping("/image/{eventId}")
    public EventDto uploadEventImage( @RequestParam(value = "image") MultipartFile image, @PathVariable Long eventId) {
        return eventService.uploadEventImage(eventId, image);
    }

    @PutMapping
    public EventDto updateEvent(@RequestBody EventUpdateDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    @GetMapping("/library/{libraryId}")
    public EventResponse findAllByLibraryId(@PathVariable Long libraryId,
                                            @RequestParam(value = "streamId", required = false) Long streamId,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                            @RequestParam(value = "eventName", defaultValue = "") String eventName,
                                            @RequestParam(value = "userId") Long userId) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.findAllByLibraryId(userId, libraryId, streamId, eventName,  pageable);
    }

    @GetMapping("/list")
    public EventResponse findAll(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                 @RequestParam(value = "eventName", defaultValue = "") String eventName,
                                 @RequestParam(value = "userId") Long userId) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.findAll(userId, eventName, pageable);
    }

    @GetMapping("/list/user")
    public EventResponse findAllByUser(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                       @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                       @RequestParam(value = "eventName", defaultValue = "") String eventName,
                                       @RequestParam(value = "userId") Long userId) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.findAllByUserPageable(userId, eventName, pageable);
    }

    @GetMapping("/library/not/{libraryId}")
    public EventResponse findAllByLibraryIdNot(@PathVariable Long libraryId,
                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                               @RequestParam(value = "eventName", defaultValue = "") String eventName,
                                               @RequestParam(value = "userId") Long userId) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.findAllByNotLibraryId(userId, libraryId, eventName, pageable);
    }

    @GetMapping("/stream/not/{libraryId}")
    public List<EventDto> findAllByNameAndStreamNotContaining(@PathVariable Long libraryId,
                                                              @RequestParam(value = "streamId", required = false) Long streamId,
                                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                              @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                              @RequestParam(value = "eventName", defaultValue = "") String eventName,
                                                              @RequestParam(value = "userId") Long userId
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventService.findAllByNameAndStreamNotContaining(userId, libraryId, streamId, eventName, pageable);
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable Long id) {
        return eventService.findEventById(id);
    }

    @PutMapping("/favorite")
    public EventDto addEventToFavorites(@RequestParam(value = "eventId") Long eventId,
                                        @RequestParam(value = "userId") Long userId) {
        return eventService.addEventToFavorites(eventId, userId);
    }

    @DeleteMapping("/favorite")
    public EventDto removeEventFromFavorites(@RequestParam(value = "eventId") Long eventId,
                                             @RequestParam(value = "userId") Long userId) {
        return eventService.removeEventFromFavorites(eventId, userId);
    }

    @GetMapping("/favorite")
    public EventResponse getLibraryFavoriteListByUser(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "eventName", defaultValue = "") String eventName,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.getUserFavoriteEventsPaginated(userId, eventName, pageable);
    }

    @PutMapping("/visitor")
    public EventDto addVisitorToEvent(@RequestParam(value = "eventId") Long eventId,
                                        @RequestParam(value = "userId") Long userId) {
        return eventService.addVisitorToEvent(eventId, userId);
    }

    @DeleteMapping("/visitor")
    public EventDto deleteVisitorFromEvent(@RequestParam(value = "eventId") Long eventId,
                                      @RequestParam(value = "userId") Long userId) {
        return eventService.deleteVisitorFromEvent(eventId, userId);
    }

    @GetMapping("/visitors")
    public UserResponse getEventVisitors(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam Long eventId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return userService.getEventVisitors(eventId, username, pageable);
    }

    @GetMapping("/library/not-contains/{libraryId}")
    public List<EventDto> findAllByNameAndLibraryNotContaining(@PathVariable Long libraryId,
                                                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                              @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
                                                              @RequestParam(value = "eventName", defaultValue = "") String eventName,
                                                              @RequestParam(value = "userId") Long userId
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return eventService.findAllByNameAndLibraryNotContaining(userId, libraryId, eventName, pageable);
    }
}
