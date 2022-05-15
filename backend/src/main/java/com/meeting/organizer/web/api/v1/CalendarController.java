package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.CalendarService;
import com.meeting.organizer.web.dto.v1.CalendarDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(CalendarController.BASE_URL)
public class CalendarController {

    public static final String BASE_URL = "/api/v1/calendar";
    private final CalendarService calendarService;

    @GetMapping
    public List<CalendarDto> findAllByLibraryId(@RequestParam(value = "userId") Long userId) {
        return calendarService.findAllEventsByUserId(userId);
    }
}
