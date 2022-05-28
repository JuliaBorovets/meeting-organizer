package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.model.WebexUpdateMeeting;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.web.dto.v1.event.EventCreateDto;
import com.meeting.organizer.web.dto.v1.event.EventDto;
import com.meeting.organizer.web.dto.v1.event.webex.WebexEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.webex.WebexEventDto;
import com.meeting.organizer.web.dto.v1.event.webex.WebexEventUpdateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventMapper {

    Event eventFromCreateDto(EventCreateDto createDto);

    Event eventFromEventDto(EventDto eventDto);

    @Mappings({
            @Mapping(target = "streamId", source = "event.stream.streamId"),
    })
    EventDto eventToEventDto(Event event);

    ZoomMeeting zoomMeetingCreateDtoToMeeting(ZoomEventCreateDto createDto);

    ZoomMeeting zoomMeetingUpdateDtoToMeeting(ZoomEventUpdateDto updateDto);

    WebexCreateMeeting webexMeetingCreateDtoToMeeting(WebexEventCreateDto createDto);

    WebexUpdateMeeting webexMeetingUpdateDtoToMeeting(WebexEventUpdateDto eventUpdateDto);

    ZoomMeeting zoomMeetingDtoToMeeting(ZoomEventDto zoomEventDto);

    WebexMeeting zoomMeetingDtoToMeeting(WebexEventDto eventDto);

    ZoomEventDto metingToZoomMeetingDto(ZoomMeeting zoomMeeting);

    WebexEventDto meetingToWebexMeetingDto(WebexMeeting webexMeeting);
}
