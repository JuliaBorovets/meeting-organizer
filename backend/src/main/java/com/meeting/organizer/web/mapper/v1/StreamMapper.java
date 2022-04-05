package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Stream;
import com.meeting.organizer.web.dto.v1.stream.StreamCreateDto;
import com.meeting.organizer.web.dto.v1.stream.StreamDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StreamMapper {

    Stream streamDtoToStream(StreamDto streamDto);

    StreamDto streamToStreamDto(Stream stream);

    Stream streamCreateDtoToStream(StreamCreateDto createDto);
}
