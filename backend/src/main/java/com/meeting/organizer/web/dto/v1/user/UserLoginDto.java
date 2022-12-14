package com.meeting.organizer.web.dto.v1.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserLoginDto {
    private Long userId;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String token;

    private String imagePath;

    private List<String> roles;
}