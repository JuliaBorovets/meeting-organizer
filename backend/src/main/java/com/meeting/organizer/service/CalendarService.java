package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.CalendarDto;

import java.util.List;

public interface CalendarService {

    List<CalendarDto> findAllEventsByUserId(Long userId);
}
