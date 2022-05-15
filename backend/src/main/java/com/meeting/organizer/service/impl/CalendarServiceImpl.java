package com.meeting.organizer.service.impl;

import com.meeting.organizer.service.CalendarService;
import com.meeting.organizer.service.EventService;
import com.meeting.organizer.web.dto.v1.CalendarDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final EventService eventService;

    @Override
    public List<CalendarDto> findAllEventsByUserId(Long userId) {
        return eventService.findAllByUser(userId).stream()
                .map(e -> CalendarDto.builder()
                        .id(e.getEventId())
                        .start(e.getStartDate().toString())
                        .end(e.getEndDate().toString())
                        .url(e.getJoinUrl())
                        .title(e.getName()).build())
                .collect(Collectors.toList());
    }
}
