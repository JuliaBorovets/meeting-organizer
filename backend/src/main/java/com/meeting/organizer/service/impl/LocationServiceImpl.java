package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.LocationNotFoundException;
import com.meeting.organizer.model.Location;
import com.meeting.organizer.repository.LocationRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.LocationService;
import com.meeting.organizer.web.dto.v1.location.LocationCreateDto;
import com.meeting.organizer.web.dto.v1.location.LocationDto;
import com.meeting.organizer.web.dto.v1.location.LocationUpdateDto;
import com.meeting.organizer.web.mapper.v1.LocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class LocationServiceImpl extends AbstractService<Location, LocationRepository> implements LocationService {

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository repository,
                               LocationRepository locationRepository,
                               LocationMapper locationMapper) {
        super(repository);
        this.locationMapper = locationMapper;
    }

    @Transactional
    @Override
    public LocationDto saveNewLocationDto(LocationCreateDto locationDto) {
        Location location = locationMapper.locationCreateDtoToLocation(locationDto);
        log.debug("Creating new location: {}", location);
        return locationMapper.locationToLocationDto(
                super.save(location)
        );
    }

    @Transactional
    @Override
    public LocationDto updateLocationDto(LocationUpdateDto locationDto) {
        Optional<Location> optionalLocation = repository.findById(locationDto.getLocationId());

        if (!optionalLocation.isPresent()) {
            throw new LocationNotFoundException("Cannot find location with id=" + locationDto.getLocationId());
        }

        Location location = optionalLocation.get();
        location.setCity(locationDto.getCity());
        location.setCountry(locationDto.getCountry());

        log.debug("Updating location: {}", location);
        return locationMapper.locationToLocationDto(
                super.save(location)
        );
    }

    @Transactional
    @Override
    public void deleteLocation(Long id) {
        repository.findById(id).ifPresentOrElse(
                (location) -> {
                    log.debug("Deleting location with id={}", location.getLocationId());
                    repository.deleteById(location.getLocationId());
                },
                () -> {
                    throw new LocationNotFoundException("Canot find location with id=" + id);
                });
    }

}
