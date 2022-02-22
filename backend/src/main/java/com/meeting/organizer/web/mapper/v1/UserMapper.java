package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.user.User;
import com.meeting.organizer.web.dto.v1.user.UserCreateDto;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {

    UserDto userToUserDto(User user);

    User createDtoToUser(UserCreateDto createDto);
}