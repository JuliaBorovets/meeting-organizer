package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.stream.StreamCreateDto;
import com.meeting.organizer.web.dto.v1.stream.StreamDto;
import com.meeting.organizer.web.dto.v1.stream.StreamResponse;
import com.meeting.organizer.web.dto.v1.stream.StreamUpdateDto;
import org.springframework.data.domain.Pageable;

public interface StreamService {

    StreamDto createNewStream(StreamCreateDto createDto);

    StreamDto updateStream(StreamUpdateDto updateDto);

    void deleteStreamById(Long id);

    StreamDto findStreamById(Long id);

    StreamResponse getAllByLibraryIdPaginated(Long libraryId, Pageable pageable);

    StreamDto addEventToStream(Long streamId, Long eventId);

    StreamDto deleteEventFromStream(Long streamId, Long eventId);
}
