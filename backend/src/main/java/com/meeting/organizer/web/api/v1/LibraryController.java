package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.LibraryService;
import com.meeting.organizer.web.dto.v1.library.*;
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
        libraryService.deleteById(id);
    }

    @GetMapping("/{id}")
    public LibraryDto getById(@PathVariable Long id) {
        return libraryService.getLibraryDtoById(id);
    }

    @GetMapping
    public LibraryResponse getLibraryList(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "libraryName", defaultValue = "") String libraryName,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return libraryService.getLibraryListPaginated(userId, libraryName, pageable);
    }

    @GetMapping("/list")
    public LibraryResponse getLibraryListByUser(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "libraryName", defaultValue = "") String libraryName,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return libraryService.getUserLibraryListPaginated(userId, libraryName, pageable);
    }


    @PutMapping("/favorite")
    public LibraryDto addLibraryToFavorites(@RequestParam(value = "libraryId") Long libraryId,
                                            @RequestParam(value = "userId") Long userId) {
        return libraryService.addLibraryToFavorites(libraryId, userId);
    }

    @DeleteMapping("/favorite")
    public LibraryDto removeLibraryFromFavorites(@RequestParam(value = "libraryId") Long libraryId,
                                                 @RequestParam(value = "userId") Long userId) {
        return libraryService.removeLibraryFromFavorites(libraryId, userId);
    }

    @GetMapping("/favorite")
    public LibraryResponse getLibraryFavoriteListByUser(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "libraryName", defaultValue = "") String libraryName,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return libraryService.getUserFavoriteLibrariesPaginated(userId, libraryName, pageable);
    }

    @PatchMapping("/add_event")
    public LibraryDto addEventToLibrary(@RequestBody AddEventToLibraryDto eventIdList) {
        return libraryService.addEventToLibrary(eventIdList);
    }

    @PatchMapping("/remove_event")
    public LibraryDto deleteEventFromLibrary(@RequestParam(value = "libraryId") Long libraryId,
                                             @RequestParam(value = "eventId") Long eventId) {
        return libraryService.deleteEventFromLibrary(libraryId, eventId);
    }

}
