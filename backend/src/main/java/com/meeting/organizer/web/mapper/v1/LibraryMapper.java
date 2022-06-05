package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.web.dto.v1.library.LibraryCreateDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface LibraryMapper {

    Library libraryCreateDtoToLibrary(LibraryCreateDto libraryCreateDto);

    @Mappings({
            @Mapping(target = "userId", source = "library.user.userId"),
    })
    LibraryDto libraryToLibraryDto(Library library);
}