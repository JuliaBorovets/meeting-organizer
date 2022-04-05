package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.LocationService;
import com.meeting.organizer.web.dto.v1.location.LocationCreateDto;
import com.meeting.organizer.web.dto.v1.location.LocationDto;
import com.meeting.organizer.web.dto.v1.location.LocationUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(LocationController.BASE_URL)
@RequiredArgsConstructor
public class LocationController {

    public static final String BASE_URL = "/api/v1/location";
    private final LocationService locationService;

    @PostMapping
    public LocationDto create(@RequestBody LocationCreateDto locationCreateDto) {
        return locationService.saveNewLocationDto(locationCreateDto);
    }

    @PutMapping
    public LocationDto update(@RequestBody LocationUpdateDto locationUpdateDto) {
        return locationService.updateLocationDto(locationUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }

}
