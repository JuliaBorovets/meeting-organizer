package com.meeting.organizer.service.impl;

import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.client.zoom.service.ZoomClientService;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.MeetingType;
import com.meeting.organizer.model.Stream;
import com.meeting.organizer.repository.EventRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.EventService;
import com.meeting.organizer.web.dto.v1.event.*;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventDto;
import com.meeting.organizer.web.mapper.v1.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl extends AbstractService<Event, EventRepository> implements EventService {

    private final ZoomClientService zoomClientService;
    private final EventMapper eventMapper;
    private final CRUDService<Library> libraryService;
    private final CRUDService<Stream> streamService;

    public EventServiceImpl(EventRepository eventRepository,
                            ZoomClientService zoomClientService,
                            EventMapper eventMapper,
                            @Qualifier("libraryServiceImpl") CRUDService<Library> libraryService,
                            @Lazy @Qualifier("streamServiceImpl") CRUDService<Stream> streamService) {
        super(eventRepository);
        this.zoomClientService = zoomClientService;
        this.eventMapper = eventMapper;
        this.libraryService = libraryService;
        this.streamService = streamService;
    }

    @Transactional
    @Override
    public EventDto createEvent(EventCreateDto eventCreateDto) {
        Event event = eventMapper.eventFromCreateDto(eventCreateDto);
        Library library = libraryService.findById(eventCreateDto.getLibraryId());
        Stream stream = null;
        event.setLibrary(library);

        if (Objects.nonNull(eventCreateDto.getStreamId())) {
            stream = streamService.findById(eventCreateDto.getStreamId());
            event.setStream(stream);
        }

        Event savedEvent = repository.save(event);
        libraryService.save(library);

        if (Objects.nonNull(stream)) {
            streamService.save(stream);
        }

        createMeetingByType(eventCreateDto.getMeetingType(), eventCreateDto.getMeetingEntity());

        return eventMapper.eventToEventDto(savedEvent);
    }

    @Transactional
    @Override
    public EventDto updateEvent(EventUpdateDto eventUpdateDto) {

        Event updatedEvent = findById(eventUpdateDto.getEventId());
        updatedEvent.setStartDate(eventUpdateDto.getStartDate());
        updatedEvent.setEndDate(eventUpdateDto.getEndDate());
        updatedEvent.setMaxNumberParticipants(eventUpdateDto.getMaxNumberParticipants());
        updatedEvent.setName(eventUpdateDto.getName());
        updatedEvent.setPhoto(eventUpdateDto.getPhoto());
        updatedEvent.setMeetingType(eventUpdateDto.getMeetingType());

        repository.save(updatedEvent);

        updateMeetingByType(eventUpdateDto.getMeetingType(), eventUpdateDto.getMeetingEntity());

        return eventMapper.eventToEventDto(updatedEvent);
    }

    @Transactional
    @Override
    public void deleteEvent(Long id) {
        log.info("deleting event with={}", id);
        Event event = findById(id);
        deleteMeetingByType(event.getMeetingType(), event.getExternalMeetingId());
        super.deleteById(id);
    }

    @Override
    public EventResponse findAllByLibraryId(Long libraryId, Long streamId, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();

        List<EventDto> eventDtoList = repository.findByLibrary_LibraryIdAndStream_StreamId(libraryId, streamId, pageable)
                .stream()
                .map(eventMapper::eventToEventDto)
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(countAllByLibraryId(libraryId));

        return eventResponse;
    }

    @Override
    public EventDto findEventById(Long eventId) {
        return eventMapper.eventToEventDto(
                findById(eventId)
        );
    }

    private void createMeetingByType(MeetingType type, MeetingDto meetingDto) {
        if (type.equals(MeetingType.ZOOM) && Objects.nonNull(meetingDto)) {
            ZoomEventCreateDto zoomEventCreateDto = (ZoomEventCreateDto) meetingDto;
            ZoomMeeting zoomMeeting = eventMapper.meetingCreateDtoToMeeting(zoomEventCreateDto);
            log.info("zoomMeeting {}", zoomMeeting);
            zoomClientService.createMeeting(zoomMeeting);
        }
    }

    private void updateMeetingByType(MeetingType type, MeetingDto meetingDto) {
        if (type.equals(MeetingType.ZOOM) && Objects.nonNull(meetingDto)) {
            ZoomEventDto zoomEventUpdateDto = (ZoomEventDto) meetingDto;
            ZoomMeeting zoomMeeting = eventMapper.meetingDtoToMeeting(zoomEventUpdateDto);
            log.info("zoomMeeting {}", zoomMeeting);
            zoomClientService.updateMeeting(zoomMeeting);
        }
    }

    private void deleteMeetingByType(MeetingType type, Long meetingId) {
        if (type.equals(MeetingType.ZOOM)) {
            zoomClientService.deleteMeeting(meetingId);
        }
    }

    private Long countAllByLibraryId(Long libraryId) {
        return repository.countByLibrary_LibraryId(libraryId);
    }

}
