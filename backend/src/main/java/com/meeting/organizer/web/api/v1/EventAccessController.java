package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.EventService;
import com.meeting.organizer.web.dto.v1.event.AddEventAccessByTokenDto;
import com.meeting.organizer.web.dto.v1.event.AddEventAccessDto;
import com.meeting.organizer.web.dto.v1.event.EventDto;
import com.meeting.organizer.web.dto.v1.event.EventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(EventAccessController.BASE_URL)
@RequiredArgsConstructor
public class EventAccessController {

    public static final String BASE_URL = "/api/v1/event/access";
    private final EventService eventService;

    @GetMapping
    public EventResponse getEventsGivenAccessListByUser(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "eventName", defaultValue = "") String eventName,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return eventService.getEventsGivenAccessListByUser(userId, eventName, pageable);
    }

    @PutMapping
    public EventDto addAccessToEventByUserEmail(@RequestBody AddEventAccessDto addEventAccessDto) {
        return eventService.addAccessToEventByUserEmail(addEventAccessDto);
    }

    @DeleteMapping
    public EventDto removeAccessToLibraryByUserEmail(@RequestParam(value = "emailList") List<String> emailList,
                                                       @RequestParam(value = "eventId")  Long eventId) {
        return eventService.removeAccessFromEventByUserEmail(emailList, eventId);
    }

    @PutMapping("/token")
    public EventDto addAccessToLibraryByToken(@RequestBody AddEventAccessByTokenDto addEventAccessDto) {
        return eventService.addAccessToEventByToken(addEventAccessDto);
    }

}
