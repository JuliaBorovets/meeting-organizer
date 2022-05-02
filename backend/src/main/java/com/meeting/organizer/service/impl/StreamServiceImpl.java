package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.StreamNotFoundException;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.Stream;
import com.meeting.organizer.repository.StreamRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.StreamService;
import com.meeting.organizer.web.dto.v1.event.AddEventToStreamDto;
import com.meeting.organizer.web.dto.v1.stream.StreamCreateDto;
import com.meeting.organizer.web.dto.v1.stream.StreamDto;
import com.meeting.organizer.web.dto.v1.stream.StreamResponse;
import com.meeting.organizer.web.dto.v1.stream.StreamUpdateDto;
import com.meeting.organizer.web.mapper.v1.StreamMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StreamServiceImpl extends AbstractService<Stream, StreamRepository> implements StreamService {

    private final StreamMapper streamMapper;
    private final CRUDService<Library> libraryService;
    private final CRUDService<Event> eventService;

    public StreamServiceImpl(StreamRepository repository,
                             StreamMapper streamMapper,
                             @Qualifier("libraryServiceImpl") CRUDService<Library> libraryService,
                             @Qualifier("eventServiceImpl") CRUDService<Event> eventService) {
        super(repository);
        this.streamMapper = streamMapper;
        this.libraryService = libraryService;
        this.eventService = eventService;
    }

    @Transactional
    @Override
    public StreamDto createNewStream(StreamCreateDto createDto) {
        log.info("Creating new Stream: {}", createDto);

        Stream streamEntity = streamMapper.streamCreateDtoToStream(createDto);
        Library library = libraryService.findById(createDto.getLibraryId());
        streamEntity.setLibrary(library);
        Stream createdEntity = repository.save(streamEntity);
        log.info("created stream {}", createdEntity);

        library.getStreams().add(createdEntity);
        libraryService.save(library);
        return streamToStreamDto(createdEntity);
    }

    @Transactional
    @Override
    public StreamDto updateStream(StreamUpdateDto updateDto) {
        Stream stream = findById(updateDto.getStreamId());
        stream.setName(updateDto.getName());
        repository.save(stream);
        return streamToStreamDto(stream);
    }

    //todo add onDelete relation configuration to delete all inner events
    @Transactional
    @Override
    public void deleteStreamById(Long id) {
        super.deleteById(id);
    }

    @Override
    public StreamDto findStreamById(Long id) {
        Stream stream = repository.findById(id)
                .orElseThrow(() -> new StreamNotFoundException("Stream not found. id=" + id));
        return streamToStreamDto(stream);
    }

    @Override
    public StreamResponse getAllByLibraryIdPaginated(Long libraryId, Pageable pageable) {

        StreamResponse response = new StreamResponse();

        List<StreamDto> streamDtoList = repository.findByLibrary_LibraryId(libraryId, pageable)
                .stream()
                .map(this::streamToStreamDto)
                .collect(Collectors.toList());

        response.setList(streamDtoList);
        response.setTotalItems((long) streamDtoList.size());

        return response;
    }

    @Transactional
    @Override
    public StreamDto addEventToStream(AddEventToStreamDto addEventToStreamDto) {

        Stream stream = findById(addEventToStreamDto.getStreamId());
        List<Event> eventList = addEventToStreamDto.getEventIdsList().stream()
                .map(eventService::findById)
                .peek(event -> event.setStream(stream))
                .collect(Collectors.toList());

        log.info("{}", eventList);
        stream.getEvents().addAll(eventList);

        repository.save(stream);
        eventList.forEach(eventService::save);
        return streamToStreamDto(stream);
    }

    @Transactional
    @Override
    public StreamDto deleteEventFromStream(Long streamId, Long eventId) {
        Stream stream = findById(streamId);
        Event event = eventService.findById(eventId);

        stream.getEvents().remove(event);
        event.setStream(null);

        repository.save(stream);
        eventService.save(event);
        return streamToStreamDto(stream);
    }

    private StreamDto streamToStreamDto(Stream stream) {
        StreamDto streamDto = streamMapper.streamToStreamDto(stream);
        streamDto.setLibraryId(stream.getLibrary().getLibraryId());
        return streamDto;
    }
}