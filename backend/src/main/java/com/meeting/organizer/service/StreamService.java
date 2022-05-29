package com.meeting.organizer.service;

import com.meeting.organizer.model.Stream;
import com.meeting.organizer.web.dto.v1.event.AddEventToStreamDto;
import com.meeting.organizer.web.dto.v1.stream.StreamCreateDto;
import com.meeting.organizer.web.dto.v1.stream.StreamDto;
import com.meeting.organizer.web.dto.v1.stream.StreamResponse;
import com.meeting.organizer.web.dto.v1.stream.StreamUpdateDto;
import org.springframework.data.domain.Pageable;

public interface StreamService extends CRUDService<Stream> {

    StreamDto createNewStream(StreamCreateDto createDto);

    StreamDto updateStream(StreamUpdateDto updateDto);

    StreamDto findStreamById(Long id);

    StreamResponse getAllByLibraryIdPaginated(Long libraryId, String streamName, Pageable pageable);

    StreamDto addEventToStream(AddEventToStreamDto eventList);

    StreamDto deleteEventFromStream(Long streamId, Long eventId);
}
