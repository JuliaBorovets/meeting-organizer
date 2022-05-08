package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.LibraryService;
import com.meeting.organizer.web.dto.v1.library.AddLibraryAccessByTokenDto;
import com.meeting.organizer.web.dto.v1.library.AddLibraryAccessDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import com.meeting.organizer.web.dto.v1.library.LibraryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(LibraryAccessController.BASE_URL)
@RequiredArgsConstructor
public class LibraryAccessController {

    public static final String BASE_URL = "/api/v1/library/access";
    private final LibraryService libraryService;

    @GetMapping
    public LibraryResponse getLibraryGivenAccessListByUser(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @RequestParam Long userId) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return libraryService.getLibraryGivenAccessListByUser(userId, pageable);
    }

    @PutMapping
    public LibraryDto addAccessToLibraryByUserEmail(@RequestBody AddLibraryAccessDto addLibraryAccessDto) {
        return libraryService.addAccessToLibraryByUserEmail(addLibraryAccessDto);
    }

    @DeleteMapping
    public LibraryDto removeAccessToLibraryByUserEmail(@RequestParam(value = "emailList") List<String> emailList,
                                                       @RequestParam(value = "libraryId")  Long libraryId) {
        return libraryService.removeAccessToLibraryByUserEmail(emailList, libraryId);
    }

    @PutMapping("/token")
    public LibraryDto addAccessToLibraryByToken(@RequestBody AddLibraryAccessByTokenDto addLibraryAccessDto) {
        return libraryService.addAccessToLibraryByToken(addLibraryAccessDto);
    }
}
