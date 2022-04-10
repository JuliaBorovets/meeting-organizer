package com.meeting.organizer.web.dto.v1.user;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {

    private String email;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

}
