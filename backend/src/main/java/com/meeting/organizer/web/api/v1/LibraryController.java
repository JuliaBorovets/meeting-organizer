package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.LibraryService;
import com.meeting.organizer.web.dto.v1.library.LibraryCreateDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import com.meeting.organizer.web.dto.v1.library.LibraryResponse;
import com.meeting.organizer.web.dto.v1.library.LibraryUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(LibraryController.BASE_URL)
@RequiredArgsConstructor
public class LibraryController {

    public static final String BASE_URL = "/api/v1/library";
    private final LibraryService libraryService;

    @PostMapping
    public LibraryDto create(@RequestBody LibraryCreateDto libraryCreateDto) {
        return libraryService.createLibrary(libraryCreateDto);
    }

    @PutMapping
    public LibraryDto update(@RequestBody LibraryUpdateDto libraryUpdateDto) {
        return libraryService.updateLibrary(libraryUpdateDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
    }

    @GetMapping("/{id}")
    public LibraryDto getById(@PathVariable Long id) {
        return libraryService.getLibraryDtoById(id);
    }

    @GetMapping
    public LibraryResponse getLibraryList(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return libraryService.getLibraryListPaginated(pageable);
    }

    @GetMapping("/list")
    public LibraryResponse getLibraryListByUser(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return libraryService.getUserLibraryListPaginated(userId, pageable);
    }
}
