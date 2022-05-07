package com.meeting.organizer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.service.WebexClientService;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.client.zoom.service.ZoomClientService;
import com.meeting.organizer.exception.custom.UnsupportedEventException;
import com.meeting.organizer.model.*;
import com.meeting.organizer.repository.EventRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.EventService;
import com.meeting.organizer.web.dto.v1.event.*;
import com.meeting.organizer.web.dto.v1.event.webex.WebexEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventDto;
import com.meeting.organizer.web.mapper.v1.EventMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl extends AbstractService<Event, EventRepository> implements EventService {

    private final ZoomClientService zoomClientService;
    private final WebexClientService webexClientService;
    private final EventMapper eventMapper;
    private final CRUDService<Library> libraryService;
    private final CRUDService<Stream> streamService;

    public EventServiceImpl(EventRepository eventRepository,
                            ZoomClientService zoomClientService,
                            WebexClientService webexClientService,
                            EventMapper eventMapper,
                            @Qualifier("libraryServiceImpl") CRUDService<Library> libraryService,
                            @Lazy @Qualifier("streamServiceImpl") CRUDService<Stream> streamService) {
        super(eventRepository);
        this.zoomClientService = zoomClientService;
        this.webexClientService = webexClientService;
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

        if (eventCreateDto.getGenerateMeeting()) {
            createMeetingByType(
                    eventCreateDto.getMeetingType(),
                    eventCreateDto.getMeetingEntity(),
                    event);
        } else {
            event.setJoinUrl(eventCreateDto.getJoinUrl());
        }

        Event savedEvent = repository.save(event);
        libraryService.save(library);

        if (Objects.nonNull(stream)) {
            streamService.save(stream);
        }


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
        //todo delete also external meeting
        //deleteMeetingByType(event.getMeetingType(), event.getExternalMeetingId());
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
    public EventResponse findAllByNotLibraryId(Long libraryId, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();

        List<EventDto> eventDtoList = repository.findByLibrary_LibraryIdNotContaining(libraryId, pageable)
                .stream()
                .map(eventMapper::eventToEventDto)
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(countAllByLibraryId(libraryId));

        return eventResponse;
    }

    @Override
    public EventDto findEventById(Long eventId) {
        EventDto eventDto = eventMapper.eventToEventDto(
                findById(eventId)
        );

        setExternalMeetingByType(eventDto);

        return eventDto;
    }

    @Override
    public List<EventDto> findAllByNameAndStreamNotContaining(Long libraryId, Long streamId, String name, Pageable pageable) {

        List<Event> result = repository.findByLibrary_LibraryIdAndStream_StreamIdAndNameLike(
                libraryId, null, name + "%", pageable);

        return result.stream()
                .map(eventMapper::eventToEventDto)
                .collect(Collectors.toList());
    }

    private void setExternalMeetingByType(EventDto eventDto) {
        MeetingType eventType = eventDto.getMeetingType();

        if (eventType.equals(MeetingType.ZOOM)) {
            ZoomMeeting zoomMeeting = zoomClientService.getById(Long.parseLong(eventDto.getExternalMeetingId()));
            eventDto.setMeetingEntity(
                    eventMapper.metingToZoomMeetingDto(zoomMeeting)
            );
            return;
        }

        if (eventType.equals(MeetingType.WEBEX)) {
            WebexMeeting webexMeeting = webexClientService.getById(eventDto.getExternalMeetingId());
            eventDto.setMeetingEntity(
                    eventMapper.meetingToWebexMeetingDto(webexMeeting)
            );
            return;
        }

        throw new UnsupportedEventException("Not supported type for event " + eventType.name());

    }

    @SneakyThrows
    private void createMeetingByType(MeetingType type, Object meetingDto, Event event) {
        if (type.equals(MeetingType.ZOOM) && Objects.nonNull(meetingDto)) {
            ObjectMapper mapper = new ObjectMapper();

            ZoomEventCreateDto zoomEventCreateDto = mapper.readValue(mapper.writeValueAsString(meetingDto), ZoomEventCreateDto.class);

            ZoomMeeting zoomMeeting = eventMapper.zoomMeetingCreateDtoToMeeting(zoomEventCreateDto);

            log.info("zoomMeeting {}", zoomMeeting);

            ZoomMeeting createdMeeting = zoomClientService.createMeeting(zoomMeeting);
            event.setExternalMeetingId(createdMeeting.getId().toString());
            event.setJoinUrl(createdMeeting.getJoin_url());
            return;
        }

        if (type.equals(MeetingType.WEBEX) && Objects.nonNull(meetingDto)) {
            ObjectMapper mapper = new ObjectMapper();

            WebexEventCreateDto webexEventCreateDto = mapper.readValue(mapper.writeValueAsString(meetingDto), WebexEventCreateDto.class);


            DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnn'Z'");
            String end = LocalDateTime.parse(webexEventCreateDto.getStart(), FORMATTER)
                    .plus(webexEventCreateDto.getDurationInMinutes(), ChronoUnit.MINUTES)
                    .toString();

            WebexCreateMeeting webexMeeting = eventMapper.webexMeetingCreateDtoToMeeting(webexEventCreateDto);
            webexMeeting.setEnd(end);

            WebexMeeting createdMeeting = webexClientService.createMeeting(webexMeeting);
            event.setExternalMeetingId(createdMeeting.getId());
            event.setJoinUrl(createdMeeting.getWebLink());
            return;
        }

        throw new UnsupportedEventException("Not supported type for event " + type.name());
    }

    private void updateMeetingByType(MeetingType type, MeetingDto meetingDto) {
        if (type.equals(MeetingType.ZOOM) && Objects.nonNull(meetingDto)) {
            ZoomEventDto zoomEventUpdateDto = (ZoomEventDto) meetingDto;
            ZoomMeeting zoomMeeting = eventMapper.zoomMeetingDtoToMeeting(zoomEventUpdateDto);
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
