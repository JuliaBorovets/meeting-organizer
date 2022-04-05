package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.location.LocationCreateDto;
import com.meeting.organizer.web.dto.v1.location.LocationDto;
import com.meeting.organizer.web.dto.v1.location.LocationUpdateDto;

public interface LocationService {

    LocationDto saveNewLocationDto(LocationCreateDto locationDto);

    LocationDto updateLocationDto(LocationUpdateDto locationDto);

    void deleteLocation(Long id);

}
