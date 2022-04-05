package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Location;
import com.meeting.organizer.web.dto.v1.location.LocationCreateDto;
import com.meeting.organizer.web.dto.v1.location.LocationDto;
import com.meeting.organizer.web.dto.v1.location.LocationUpdateDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location locationCreateDtoToLocation(LocationCreateDto locationCreateDto);

    Location locationUpdateDtoToLocation(LocationUpdateDto locationUpdateDto);

    LocationDto locationToLocationDto(Location location);
}
