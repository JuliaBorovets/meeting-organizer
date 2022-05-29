package com.meeting.organizer.service;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.web.dto.v1.event.AddEventToStreamDto;
import com.meeting.organizer.web.dto.v1.library.*;
import com.meeting.organizer.web.dto.v1.stream.StreamDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LibraryService extends CRUDService<Library> {

    LibraryDto createLibrary(LibraryCreateDto libraryDto);

    LibraryDto updateLibrary(LibraryUpdateDto libraryDto);

    LibraryDto getLibraryDtoById(Long id);

    LibraryResponse getUserLibraryListPaginated(Long userId, String libraryName, Pageable pageable);

    LibraryResponse getLibraryListPaginated(Long userId, String libraryName, Pageable pageable);

    LibraryDto addLibraryToFavorites(Long libraryId, Long userId);

    LibraryDto removeLibraryFromFavorites(Long libraryId, Long userId);

    LibraryResponse getUserFavoriteLibrariesPaginated(Long userId, String libraryName, Pageable pageable);

    LibraryDto addAccessToLibraryByUserEmail(AddLibraryAccessDto addLibraryAccessDto);

    LibraryDto removeAccessToLibraryByUserEmail(List<String> emailList, Long libraryId);

    LibraryDto addAccessToLibraryByToken(AddLibraryAccessByTokenDto addLibraryAccessDto);

    LibraryResponse getLibraryGivenAccessListByUser(Long userId, String libraryName, Pageable pageable);

    LibraryDto addEventToLibrary(AddEventToLibraryDto eventList);

    LibraryDto deleteEventFromLibrary(Long libraryId, Long eventId);

}
