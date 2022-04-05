package com.meeting.organizer.web.dto.v1;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoZoom {

    String email;

    String firstName;

    String lastName;

    String password;

    Integer type;
}
