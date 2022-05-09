package com.meeting.organizer.service;

import com.meeting.organizer.model.user.User;
import com.meeting.organizer.web.dto.v1.user.UserCreateDto;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import com.meeting.organizer.web.dto.v1.user.UserUpdateDto;

public interface UserService {

    UserDto saveNewUserDto(UserCreateDto userDto);

    User findByEmail(String email);

    void confirmRegistration(String token);

    void resendRegistrationConfirmLink(String token);

    UserDto getById(Long id);

    UserDto update(UserUpdateDto userUpdateDto);
}
