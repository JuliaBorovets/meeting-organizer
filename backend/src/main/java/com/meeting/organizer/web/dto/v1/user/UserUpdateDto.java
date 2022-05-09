package com.meeting.organizer.web.dto.v1.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {

    private Long userId;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private String password;
}
