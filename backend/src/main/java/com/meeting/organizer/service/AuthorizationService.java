package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.user.UserDto;
import com.meeting.organizer.web.dto.v1.user.UserLoginDto;

public interface AuthorizationService {

    UserLoginDto authenticate(UserDto userDto);
}
