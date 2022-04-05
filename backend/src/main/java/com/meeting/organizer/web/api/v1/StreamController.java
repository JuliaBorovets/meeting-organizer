package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.StreamService;
import com.meeting.organizer.web.dto.v1.stream.StreamCreateDto;
import com.meeting.organizer.web.dto.v1.stream.StreamDto;
import com.meeting.organizer.web.dto.v1.stream.StreamResponse;
import com.meeting.organizer.web.dto.v1.stream.StreamUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(StreamController.BASE_URL)
@RequiredArgsConstructor
public class StreamController {

    public static final String BASE_URL = "/api/v1/stream";

    private final StreamService streamService;

    @PostMapping
    public StreamDto create(@RequestBody StreamCreateDto streamCreateDto) {
        return streamService.createNewStream(streamCreateDto);
    }

    @PutMapping
    public StreamDto update(@RequestBody StreamUpdateDto streamUpdateDto) {
        return streamService.updateStream(streamUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        streamService.deleteStreamById(id);
    }

    @GetMapping("/{id}")
    public StreamDto getById(@PathVariable Long id) {
        return streamService.findStreamById(id);
    }

    @GetMapping("/list/{libraryId}")
    public StreamResponse getAllByLibraryId(@PathVariable Long libraryId,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return streamService.getAllByLibraryIdPaginated(libraryId, pageable);
    }

    @PatchMapping("/add_event")
    public StreamDto addEventToStream(@RequestParam(value = "streamId") Long streamId,
                                      @RequestParam(value = "eventId") Long eventId) {
        return streamService.addEventToStream(streamId, eventId);
    }

    @PatchMapping("/remove_event")
    public StreamDto deleteEventToStream(@RequestParam(value = "streamId") Long streamId,
                                         @RequestParam(value = "eventId") Long eventId) {
        return streamService.deleteEventFromStream(streamId, eventId);
    }
}
