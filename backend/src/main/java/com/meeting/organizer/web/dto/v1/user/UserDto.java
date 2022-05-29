package com.meeting.organizer.web.dto.v1.user;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @EqualsAndHashCode.Include
    private Long userId;

    private String email;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String imagePath;

    private List<RoleDto> roles = new ArrayList<>();

}