package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.EventService;
import com.meeting.organizer.web.dto.v1.event.EventCreateDto;
import com.meeting.organizer.web.dto.v1.event.EventDto;
import com.meeting.organizer.web.dto.v1.event.EventResponse;
import com.meeting.organizer.web.dto.v1.event.EventUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(EventController.BASE_URL)
public class EventController {

    public static final String BASE_URL = "/api/v1/event";
    private final EventService eventService;

    @PostMapping
    public EventDto createEvent(@RequestBody EventCreateDto eventDto) {
        return eventService.createEvent(eventDto);
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
                                            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.findAllByLibraryId(libraryId, streamId, pageable);
    }

    @GetMapping("/{id}")
    public EventDto getById(@PathVariable Long id) {
        return eventService.findEventById(id);
    }
}
