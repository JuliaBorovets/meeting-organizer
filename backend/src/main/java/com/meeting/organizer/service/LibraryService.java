package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.library.LibraryCreateDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import com.meeting.organizer.web.dto.v1.library.LibraryResponse;
import com.meeting.organizer.web.dto.v1.library.LibraryUpdateDto;
import org.springframework.data.domain.Pageable;

public interface LibraryService {

    LibraryDto createLibrary(LibraryCreateDto libraryDto);

    LibraryDto updateLibrary(LibraryUpdateDto libraryDto);

    void deleteLibrary(Long id);

    LibraryDto getLibraryDtoById(Long id);

    LibraryResponse getLibraryListPaginated(Long userId, Pageable pageable);

}
