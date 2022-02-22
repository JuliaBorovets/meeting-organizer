package com.meeting.organizer.web.dto.v1.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class UserLoginDto {
    private Long id;

    private String username;

    private String email;

    private String token;

    private List<String> roles;
}