package com.meeting.organizer.web.dto.v1.user;

import com.meeting.organizer.utils.BaseUIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserResponse extends BaseUIResponse<UserDto> {
}
