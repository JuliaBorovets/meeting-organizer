package com.meeting.organizer.web.dto.v1.user;

import lombok.*;

import java.sql.Timestamp;
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

    private List<RoleDto> roles = new ArrayList<>();

    private Timestamp lastModifiedDate;

    private Boolean userGoogle2fa = false;

    private Boolean google2faRequired = true;

}