package com.meeting.organizer.client.webex.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebexCreateMeeting {
    private String title;

    private String agenda;

    private String password;

    private String start;

    private String end;

    private String hostEmail;

    private Boolean enabledAutoRecordMeeting;

    private Boolean enabledJoinBeforeHost;

}
