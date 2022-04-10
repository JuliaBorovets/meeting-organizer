package com.meeting.organizer.service;

import com.meeting.organizer.model.user.User;
import com.meeting.organizer.web.dto.v1.user.UserCreateDto;
import com.meeting.organizer.web.dto.v1.user.UserDto;

public interface UserService {

    UserDto saveNewUserDto(UserCreateDto userDto);

    User findByEmail(String email);

    void confirmRegistration(String token);

    void resendRegistrationConfirmLink(String token);

}
