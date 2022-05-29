package com.meeting.organizer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.model.WebexUpdateMeeting;
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
import com.meeting.organizer.web.dto.v1.event.webex.WebexEventUpdateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventCreateDto;
import com.meeting.organizer.web.dto.v1.event.zoom.ZoomEventUpdateDto;
import com.meeting.organizer.web.mapper.v1.EventMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private final LibraryService libraryService;
    private final StreamService streamService;
    private final UserService userService;
    private final MailService mailService;
    private final FileStorageService fileStorageService;
    private final ExecutorService executorService;

    public EventServiceImpl(EventRepository eventRepository,
                            ZoomClientService zoomClientService,
                            WebexClientService webexClientService,
                            EventMapper eventMapper,
                            @Lazy LibraryService libraryService,
                            @Lazy StreamService streamService,
                            UserService userService,
                            FileStorageService fileStorageService,
                            MailService mailService,
                            ExecutorService executorService) {
        super(eventRepository);
        this.zoomClientService = zoomClientService;
        this.webexClientService = webexClientService;
        this.eventMapper = eventMapper;
        this.libraryService = libraryService;
        this.streamService = streamService;
        this.userService = userService;
        this.mailService = mailService;
        this.fileStorageService = fileStorageService;
        this.executorService = executorService;
    }

    @Transactional
    @Override
    public EventDto createEvent(EventCreateDto eventCreateDto) {
        Event event = eventMapper.eventFromCreateDto(eventCreateDto);
        event.setAccessToken(UUID.randomUUID().toString());
        event.setStartDate(event.getStartDate().plus(2, ChronoUnit.HOURS));
        event.setEndDate(event.getStartDate().plus(eventCreateDto.getDurationInMinutes(), ChronoUnit.MINUTES));
        User user = userService.findById(eventCreateDto.getUserId());
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

        userService.save(user);

        if (Objects.nonNull(stream)) {
            streamService.save(stream);
        }

        this.executorService.scheduleEventNotificationTask(savedEvent);
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
        updatedEvent.setMeetingType(eventUpdateDto.getMeetingType());
        updatedEvent.setStartDate(updatedEvent.getStartDate().plus(2, ChronoUnit.HOURS));
        updatedEvent.setEndDate(eventUpdateDto.getStartDate().plus(eventUpdateDto.getDurationInMinutes(), ChronoUnit.MINUTES).plus(2, ChronoUnit.HOURS));
        if (Objects.isNull(updatedEvent.getExternalMeetingId())) {
            updatedEvent.setJoinUrl(eventUpdateDto.getJoinUrl());
        } else {
            updateMeetingByType(eventUpdateDto.getMeetingType(), eventUpdateDto.getMeetingEntity(), updatedEvent.getExternalMeetingId());
        }
        this.executorService.cancelEventNotificationTask(updatedEvent);
        this.executorService.scheduleEventNotificationTask(updatedEvent);
        return eventMapper.eventToEventDto(updatedEvent);
    }

    @Transactional
    @Override
    public EventDto uploadEventImage(Long eventId, MultipartFile image) {
        Event event = findById(eventId);
        if (Objects.nonNull(event.getImagePath())) {
            fileStorageService.deleteFile(event.getImagePath());
        }
        event.setImagePath(fileStorageService.storeFile(image));
        return convertToDto(event);
    }

    @Transactional
    @Override
    public void deleteEvent(Long id) {
        Event event = findById(id);
        fileStorageService.deleteFile(event.getImagePath());
        if (Objects.nonNull(event.getExternalMeetingId())) {
            if (event.getMeetingType() == MeetingType.ZOOM) {
                zoomClientService.deleteMeeting(Long.parseLong(event.getExternalMeetingId()));
            } else if (event.getMeetingType() == MeetingType.WEBEX) {
                webexClientService.deleteMeeting(event.getExternalMeetingId());
            }
        }
        this.executorService.cancelEventNotificationTask(event);
        super.deleteById(id);
    }

    @Override
    public EventResponse findAllByLibraryId(Long userId, Long libraryId, Long streamId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();
        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findByLibrary_LibraryIdAndStream_StreamIdAndNameLikeOrderByCreationDateDesc(libraryId, streamId, eventNamePattern, pageable)
                .stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(repository.countByLibrary_LibraryIdAndStream_StreamIdAndNameLike(libraryId, streamId, eventNamePattern));

        return eventResponse;
    }

    @Override
    public EventResponse findAll(Long userId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();
        User user = userService.findById(userId);

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findEventsByNameLikeAndIsPrivateOrGivenAccessListContainsAndNameLikeOrUserAndNameLikeOrderByCreationDateDesc(eventNamePattern, false, user, eventNamePattern, user, eventNamePattern, pageable)
                .stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setTotalItems((repository.countByNameLikeAndIsPrivateOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(eventNamePattern, false, user, eventNamePattern, user, eventNamePattern)));
        eventResponse.setList(eventDtoList);

        return eventResponse;
    }

    @Override
    public List<Event> findAllByUser(Long userId) {
        return repository.findAllByUser_UserIdAndNameLikeOrderByCreationDateDesc(userId, "%", null);
    }

    @Override
    public EventResponse findAllByUserPageable(Long userId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findAllByUser_UserIdAndNameLikeOrderByCreationDateDesc(userId, eventNamePattern, pageable).stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(repository.countByUser_UserIdAndNameLike(userId, eventNamePattern));

        return eventResponse;
    }

    @Override
    public EventResponse findAllByNotLibraryId(Long userId, Long libraryId, String eventName, Pageable pageable) {
        EventResponse eventResponse = new EventResponse();

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findByLibrary_LibraryIdNotContainingAndNameLikeOrderByCreationDateDesc(libraryId, eventNamePattern, pageable)
                .stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(repository.countByLibrary_LibraryIdNotContainingAndNameLike(libraryId, eventNamePattern));

        return eventResponse;
    }

    @Override
    public EventDto findEventById(Long eventId) {
        Event event = findById(eventId);
        EventDto eventDto = convertToDto(
                event
        );

        setExternalMeetingByType(eventDto);
        eventDto.setParticipantCount((long) event.getVisitors().size());

        return eventDto;
    }

    @Override
    public List<EventDto> findAllByNameAndStreamNotContaining(Long userId, Long libraryId, Long streamId, String eventName, Pageable pageable) {

        String eventNamePattern = eventName + "%";

        List<Event> result = repository.findByLibrary_LibraryIdAndStream_StreamIdAndNameLikeOrderByCreationDateDesc(
                libraryId, null, eventNamePattern, pageable);

        return result.stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventDto addEventToFavorites(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userService.findById(userId);

        event.getUsersFavorite().add(user);
        user.getFavoriteEvents().add(event);

        return convertToDto(event, userId);
    }

    @Transactional
    @Override
    public EventDto removeEventFromFavorites(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userService.findById(userId);

        event.getUsersFavorite().remove(user);
        user.getFavoriteEvents().remove(event);

        return convertToDto(event, userId);
    }

    @Override
    public EventResponse getUserFavoriteEventsPaginated(Long userId, String eventName, Pageable pageable) {
        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findEventsByUsersFavorite_UserIdAndNameLikeOrderByCreationDateDesc(userId, eventNamePattern, pageable).stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        EventResponse eventResponse = new EventResponse();
        eventResponse.setTotalItems(repository.countByUsersFavorite_UserIdAndNameLike(userId, eventNamePattern));
        eventResponse.setList(eventDtoList);

        return eventResponse;
    }

    @Override
    public EventResponse getEventsGivenAccessListByUser(Long userId, String eventName, Pageable pageable) {

        String eventNamePattern = eventName + "%";

        List<EventDto> eventDtoList = repository.findEventsByGivenAccessList_UserIdAndNameLikeOrderByCreationDateDesc(userId, eventNamePattern, pageable).stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());

        EventResponse eventResponse = new EventResponse();
        eventResponse.setList(eventDtoList);
        eventResponse.setTotalItems(repository.countByGivenAccessList_UserIdAndNameLike(userId, eventNamePattern));

        return eventResponse;
    }

    @Transactional
    @Override
    public EventDto addAccessToEventByUserEmail(AddEventAccessDto addEventAccessDto) {
        Event event = findById(addEventAccessDto.getEventId());
        List<User> users = addEventAccessDto.getEmailList().stream()
                .map(userService::findByEmail)
                .collect(Collectors.toList());

        event.getGivenAccessList().addAll(users);
        users.forEach(u -> {
            u.getGivenAccessEvents().add(event);
            mailService.sendAddEventAccessMail(u, event);
        });

        return convertToDto(repository.save(event));
    }

    @Transactional
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
            mailService.sendRemoveEventAccessMail(u, event);
        });

        return convertToDto(repository.save(event));
    }

    @Transactional
    @Override
    public EventDto addAccessToEventByToken(AddEventAccessByTokenDto addEventAccessDto) {
        Event event = repository.findByAccessToken(addEventAccessDto.getAccessToken())
                .orElseThrow(() -> new MeetingNotFoundException("Cannot find event by token=" + addEventAccessDto.getAccessToken()));

        User user = userService.findById(addEventAccessDto.getUserId());

        event.getGivenAccessList().add(user);
        user.getGivenAccessEvents().add(event);

        mailService.sendAddEventAccessMail(user, event);

        return convertToDto(repository.save(event));
    }

    @Transactional
    @Override
    public EventDto addVisitorToEvent(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userService.findById(userId);

        event.getVisitors().add(user);
        user.getVisitedEvents().add(event);

        return convertToDto(event);
    }

    @Transactional
    @Override
    public EventDto deleteVisitorFromEvent(Long eventId, Long userId) {
        Event event = findById(eventId);
        User user = userService.findById(userId);

        event.getVisitors().remove(user);
        user.getVisitedEvents().remove(event);

        return convertToDto(event);
    }

    @Override
    public List<EventDto> findAllByNameAndLibraryNotContaining(Long userId, Long libraryId, String eventName, Pageable pageable) {
        String eventNamePattern = eventName + "%";

        List<Event> result = repository.findByLibrary_LibraryIdAndNameLikeOrderByCreationDateDesc(
                null, eventNamePattern, pageable);

        return result.stream()
                .map(e -> convertToDto(e, userId))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean eventExists(Long id) {
        return repository.findById(id).isPresent();
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

    @Transactional
    @SneakyThrows
    private void createMeetingByType(MeetingType type, Object meetingDto, Event event) {
        ObjectMapper mapper = new ObjectMapper();

        if (type.equals(MeetingType.ZOOM) && Objects.nonNull(meetingDto)) {
            ZoomEventCreateDto zoomEventCreateDto = mapper.readValue(mapper.writeValueAsString(meetingDto), ZoomEventCreateDto.class);

            ZoomMeeting zoomMeeting = eventMapper.zoomMeetingCreateDtoToMeeting(zoomEventCreateDto);

            zoomMeeting.setStart_time(
                    parseLocalDate(zoomMeeting.getStart_time()).toString()
            );

            ZoomMeeting createdMeeting = zoomClientService.createMeeting(zoomMeeting);
            event.setExternalMeetingId(createdMeeting.getId().toString());
            event.setJoinUrl(createdMeeting.getJoin_url());
            return;
        }

        if (type.equals(MeetingType.WEBEX) && Objects.nonNull(meetingDto)) {
            WebexEventCreateDto webexEventCreateDto = mapper.readValue(mapper.writeValueAsString(meetingDto), WebexEventCreateDto.class);

            WebexCreateMeeting webexMeeting = eventMapper.webexMeetingCreateDtoToMeeting(webexEventCreateDto);
            webexMeeting.setStart(parseLocalDate(webexEventCreateDto.getStart()).toString());
            String end = parseLocalDate(webexEventCreateDto.getStart())
                    .plus(webexEventCreateDto.getDurationInMinutes(), ChronoUnit.MINUTES)
                    .toString();
            webexMeeting.setEnd(end);

            WebexMeeting createdMeeting = webexClientService.createMeeting(webexMeeting);
            event.setExternalMeetingId(createdMeeting.getId());
            event.setJoinUrl(createdMeeting.getWebLink());
            return;
        }

        throw new UnsupportedEventException("Not supported type for event " + type.name());
    }

    @Transactional
    @SneakyThrows
    private void updateMeetingByType(MeetingType type, Object meetingDto, String externalMeetingId) {
        ObjectMapper mapper = new ObjectMapper();

        if (type.equals(MeetingType.ZOOM) && Objects.nonNull(meetingDto)) {
            ZoomEventUpdateDto zoomEventUpdateDto = mapper.readValue(mapper.writeValueAsString(meetingDto), ZoomEventUpdateDto.class);
            ZoomMeeting zoomMeeting = eventMapper.zoomMeetingUpdateDtoToMeeting(zoomEventUpdateDto);
            zoomMeeting.setStart_time(
                    parseLocalDate(zoomMeeting.getStart_time()).toString()
            );
            zoomClientService.updateMeeting(Long.parseLong(externalMeetingId), zoomMeeting);
        } else if (type.equals(MeetingType.WEBEX) && Objects.nonNull(meetingDto)) {
            WebexEventUpdateDto webexEventUpdateDto = mapper.readValue(mapper.writeValueAsString(meetingDto), WebexEventUpdateDto.class);

            WebexUpdateMeeting webexMeeting = eventMapper.webexMeetingUpdateDtoToMeeting(webexEventUpdateDto);
            webexMeeting.setStart(parseLocalDate(webexEventUpdateDto.getStart()).toString());
            String end = parseLocalDate(webexEventUpdateDto.getStart())
                    .plus(webexEventUpdateDto.getDurationInMinutes(), ChronoUnit.MINUTES)
                    .toString();
            webexMeeting.setEnd(end);

            webexClientService.updateMeeting(externalMeetingId, webexMeeting);
        }
    }

    @Transactional
    private void deleteMeetingByType(MeetingType type, Long meetingId) {
        if (type.equals(MeetingType.ZOOM)) {
            zoomClientService.deleteMeeting(meetingId);
        }
    }

    private EventDto convertToDto(Event event, Long userId) {
        Boolean isFavorite = event.getUsersFavorite().stream()
                .anyMatch(u -> userId.equals(u.getUserId()));

        EventDto eventDto = eventMapper.eventToEventDto(event);
        eventDto.setIsFavorite(isFavorite);
        eventDto.setParticipantCount((long) event.getVisitors().size());

        Duration duration = Duration.between(event.getStartDate(), event.getEndDate());
        eventDto.setDurationInMinutes(duration.toMinutes());
        return eventDto;
    }

    private EventDto convertToDto(Event event) {
        EventDto eventDto = eventMapper.eventToEventDto(event);
        Duration duration = Duration.between(event.getStartDate(), event.getEndDate());
        eventDto.setDurationInMinutes(duration.toMinutes());
        return eventDto;
    }

    private LocalDateTime parseLocalDate(String date) {
        DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnn");

        LocalDateTime parsedTime;
        try {
            parsedTime = LocalDateTime.parse(date, FORMATTER);
        } catch (DateTimeParseException e) {
            try {
                FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnn'Z'");
                parsedTime = LocalDateTime.parse(date, FORMATTER)
                        .plus(2, ChronoUnit.HOURS);
            } catch (Exception ex) {
                FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                parsedTime = LocalDateTime.parse(date, FORMATTER)
                        .plus(2, ChronoUnit.HOURS);
            }
        }
        return parsedTime;
    }

}
