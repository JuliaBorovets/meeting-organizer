package com.meeting.organizer.web.dto.v1.event.webex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebexEventUpdateDto {
    private String title;

    private String agenda;

    private String password;

    private String start;

    private Long durationInMinutes;

    private String hostEmail;

    private Boolean enabledAutoRecordMeeting;

    private Boolean enabledJoinBeforeHost;
}
