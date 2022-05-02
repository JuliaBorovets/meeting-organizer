package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.web.dto.v1.event.EventCreateDto;
import com.meeting.organizer.web.dto.v1.event.EventDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event eventFromCreateDto(EventCreateDto createDto);

    Event eventFromEventDto(EventDto eventDto);

    @Mappings({
            @Mapping(target = "streamId", source = "event.stream.streamId"),
    })
    EventDto eventToEventDto(Event event);

    ZoomMeeting meetingCreateDtoToMeeting(ZoomEventCreateDto createDto);

    ZoomMeeting meetingDtoToMeeting(ZoomEventDto zoomEventDto);

    ZoomEventDto metingToMeetingDto(ZoomMeeting zoomMeeting);
}
