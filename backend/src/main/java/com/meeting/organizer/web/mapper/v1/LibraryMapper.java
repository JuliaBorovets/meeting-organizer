package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.web.dto.v1.library.LibraryCreateDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LibraryMapper {

    LibraryDto libraryDtoToLibrary(LibraryCreateDto libraryCreateDto);

    Library libraryCreateDtoToLibrary(LibraryCreateDto libraryCreateDto);

    Library libraryDtoToLibrary(LibraryDto libraryDto);

    LibraryDto libraryToLibraryDto(Library library);
}