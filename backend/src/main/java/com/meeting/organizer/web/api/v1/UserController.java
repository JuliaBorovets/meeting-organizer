package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.AuthorizationService;
import com.meeting.organizer.service.RoleService;
import com.meeting.organizer.service.UserService;
import com.meeting.organizer.web.dto.v1.user.UserCreateDto;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import com.meeting.organizer.web.dto.v1.user.UserLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(UserController.BASE_URL)
@RequiredArgsConstructor
public class UserController {

    public static final String BASE_URL = "/api/v1/user";
    private final UserService userService;
    private final AuthorizationService authorizationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto userDto) {
        return userService.saveNewUserDto(userDto);
    }

    @PostMapping("/login")
    public UserLoginDto login(@RequestBody UserDto userDto) {
        return authorizationService.authenticate(userDto);
    }
}
