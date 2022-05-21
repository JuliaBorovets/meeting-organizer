package com.meeting.organizer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.service.WebexClientService;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;
import com.meeting.organizer.client.zoom.service.ZoomClientService;
import com.meeting.organizer.exception.custom.MeetingNotFoundException;
import com.meeting.organizer.exception.custom.UnsupportedEventException;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.MeetingType;
import com.meeting.organizer.model.Stream;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.EventRepository;
import com.meeting.organizer.service.*;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl extends AbstractService<Event, EventRepository> implements EventService {

    private final ZoomClientService zoomClientService;
    private final WebexClientService webexClientService;
    private final EventMapper eventMapper;
    private final CRUDService<Library> libraryService;
    private final CRUDService<Stream> streamService;
    private final CRUDService<User> userCRUDService;
    private final UserService userService;
    private final MailService mailService;


    public EventServiceImpl(EventRepository eventRepository,
                            ZoomClientService zoomClientService,
                            WebexClientService webexClientService,
                            EventMapper eventMapper,
                            @Qualifier("libraryServiceImpl") CRUDService<Library> libraryService,
                            @Lazy @Qualifier("streamServiceImpl") CRUDService<Stream> streamService,
                            @Qualifier("userServiceImpl") CRUDService<User> userCRUDService,
                            UserService userService,
                            MailService mailService) {
        super(eventRepository);
        this.zoomClientService = zoomClientService;
        this.webexClientService = webexClientService;
        this.eventMapper = eventMapper;
        this.libraryService = libraryService;
        this.streamService = streamService;
        this.userCRUDService = userCRUDService;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Transactional
    @Override
    public EventDto createEvent(EventCreateDto eventCreateDto) {
        Event event = eventMapper.eventFromCreateDto(eventCreateDto);
        event.setAccessToken(UUID.randomUUID().toString());
        event.setEndDate(event.getStartDate().plus(eventCreateDto.getDurationInMinutes(), ChronoUnit.MINUTES));
        User user = userCRUDService.findById(eventCreateDto.getUserId());
        event.setUser(user);
        event.getVisitors().add(user);
        user.getVisitedEvents().add(event);
        Stream stream = null;

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

        if (Objects.nonNull(eventCreateDto.getLibraryId())) {
            Library library = libraryService.findById(eventCreateDto.getLibraryId());
            event.setLibrary(library);
            libraryService.save(library);
        }

        Event savedEvent = repository.save(event);

        userCRUDService.save(user);

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
        updatedEvent.setMaxNumberParticipants(eventUpdateDto.getMaxNumberParticipants());
        updatedEvent.setName(eventUpdateDto.getName());
        updatedEvent.setDescription(eventUpdateDto.getDescription());
        updatedEvent.setPhoto(eventUpdateDto.getPhoto());
        updatedEvent.setMeetingType(eventUpdateDto.getMeetingType());
        updatedEvent.setEndDate(eventUpdateDto.getStartDate().plus(eventUpdateDto.getDurationInMinutes(), ChronoUnit.MINUTES));

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
    public EventResponse findAllByLibraryId(Long userId, Long libraryId, Long streamId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();
        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findByLibrary_LibraryIdAndStream_StreamIdAndNameLike(libraryId, streamId, eventNamePattern, pageable)
                .stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(countAllByLibraryId(libraryId));

        return eventResponse;
    }

    @Override
    public EventResponse findAll(Long userId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();
        User user = userCRUDService.findById(userId);

        String eventNamePattern = eventName + "%";

        log.info(eventNamePattern);

        List<EventDto> eventDtoList = repository.findEventsByNameLikeAndIsPrivateOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(eventNamePattern, false, user, eventNamePattern, user, eventNamePattern, pageable)
                .stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setTotalItems((long) eventDtoList.size());
        eventResponse.setList(eventDtoList);

        return eventResponse;
    }

    //todo add not only creator, but also participant
    @Override
    public List<Event> findAllByUser(Long userId) {
        return repository.findAllByUser_UserIdAndNameLike(userId, "%", null);
    }

    @Override
    public EventResponse findAllByUserPageable(Long userId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findAllByUser_UserIdAndNameLike(userId, eventNamePattern, pageable).stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems((long) eventDtoList.size());

        return eventResponse;
    }

    @Override
    public EventResponse findAllByNotLibraryId(Long userId, Long libraryId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findByLibrary_LibraryIdNotContainingAndNameLike(libraryId, eventNamePattern, pageable)
                .stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(countAllByLibraryId(libraryId));

        return eventResponse;
    }

    @Override
    public EventDto findEventById(Long eventId) {
        Event event = findById(eventId);
        EventDto eventDto = eventMapper.eventToEventDto(
                event
        );

        setExternalMeetingByType(eventDto);
        eventDto.setParticipantCount((long) event.getVisitors().size());

        return eventDto;
    }

    @Override
    public List<EventDto> findAllByNameAndStreamNotContaining(Long userId, Long libraryId, Long streamId, String eventName, Pageable pageable) {

        String eventNamePattern = eventName + "%";

        List<Event> result = repository.findByLibrary_LibraryIdAndStream_StreamIdAndNameLike(
                libraryId, null, eventNamePattern, pageable);

        return result.stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());
    }

    @Override
    public EventDto addEventToFavorites(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userCRUDService.findById(userId);

        event.getUsersFavorite().add(user);
        user.getFavoriteEvents().add(event);
        repository.save(event);
        userCRUDService.save(user);

        return convertToDto(event, userId);
    }

    @Override
    public EventDto removeEventFromFavorites(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userCRUDService.findById(userId);

        event.getUsersFavorite().remove(user);
        user.getFavoriteEvents().remove(event);
        repository.save(event);
        userCRUDService.save(user);

        return convertToDto(event, userId);
    }

    @Override
    public EventResponse getUserFavoriteEventsPaginated(Long userId, String eventName, Pageable pageable) {
        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findEventsByUsersFavorite_UserIdAndNameLike(userId, eventNamePattern, pageable).stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        EventResponse eventResponse = new EventResponse();
        eventResponse.setTotalItems((long) eventDtoList.size());
        eventResponse.setList(eventDtoList);

        return eventResponse;
    }

    @Override
    public EventResponse getEventsGivenAccessListByUser(Long userId, String eventName, Pageable pageable) {

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findEventsByGivenAccessList_UserIdAndNameLike(userId, eventNamePattern, pageable).stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        EventResponse eventResponse = new EventResponse();
        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems((long) eventDtoList.size());

        return eventResponse;
    }

    @Override
    public EventDto addAccessToEventByUserEmail(AddEventAccessDto addEventAccessDto) {
        Event event = findById(addEventAccessDto.getEventId());
        List<User> users = addEventAccessDto.getEmailList().stream()
                .map(userService::findByEmail)
                .collect(Collectors.toList());

        event.getGivenAccessList().addAll(users);
        users.forEach(u -> {
            u.getGivenAccessEvents().add(event);
            userCRUDService.save(u);
            mailService.sendAddEventAccessMail(u, event);
        });

        return convertToDto(repository.save(event));
    }

    @Override
    public EventDto removeAccessFromEventByUserEmail(List<String> emailList, Long eventId) {
        Event event = findById(eventId);

        List<User> users = emailList.stream()
                .map(userService::findByEmail)
                .collect(Collectors.toList());

        event.getGivenAccessList()
                .removeAll(users);
        users.forEach(u -> {
            u.getGivenAccessEvents().remove(event);
            userCRUDService.save(u);
            mailService.sendRemoveEventAccessMail(u, event);
        });

        return convertToDto(repository.save(event));
    }

    @Override
    public EventDto addAccessToEventByToken(AddEventAccessByTokenDto addEventAccessDto) {
        Event event = repository.findByAccessToken(addEventAccessDto.getAccessToken())
                .orElseThrow(() -> new MeetingNotFoundException("Cannot find event by token=" + addEventAccessDto.getAccessToken()));

        User user = userCRUDService.findById(addEventAccessDto.getUserId());

        event.getGivenAccessList().add(user);
        user.getGivenAccessEvents().add(event);

        userCRUDService.save(user);

        mailService.sendAddEventAccessMail(user, event);

        return convertToDto(repository.save(event));
    }

    @Transactional
    @Override
    public EventDto addVisitorToEvent(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userCRUDService.findById(userId);

        event.getVisitors().add(user);
        user.getVisitedEvents().add(event);

        return convertToDto(event);
    }

    @Transactional
    @Override
    public EventDto deleteVisitorFromEvent(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userCRUDService.findById(userId);

        event.getVisitors().remove(user);
        user.getVisitedEvents().remove(event);

        return convertToDto(event);
    }

    @Override
    public List<EventDto> findAllByNameAndLibraryNotContaining(Long userId, Long libraryId, String eventName, Pageable pageable) {
        String eventNamePattern = eventName + "%";

        List<Event> result = repository.findByLibrary_LibraryIdAndNameLike(
                null, eventNamePattern, pageable);

        return result.stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());
    }

    private void setExternalMeetingByType(EventDto eventDto) {
        if (Objects.isNull(eventDto.getExternalMeetingId())) {
            return;
        }

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

    private EventDto convertToDto(Event event, Long userId) {
        Boolean isFavorite = event.getUsersFavorite().stream()
                .anyMatch(u -> userId.equals(u.getUserId()));

        EventDto eventDto = eventMapper.eventToEventDto(event);
        eventDto.setIsFavorite(isFavorite);
        eventDto.setParticipantCount((long) event.getVisitors().size());
        return eventDto;
    }

    private EventDto convertToDto(Event event) {
        return eventMapper.eventToEventDto(event);
    }

}
