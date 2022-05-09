package com.meeting.organizer.web.dto.v1.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEventAccessByTokenDto {

    private Long userId;

    private String accessToken;
}
