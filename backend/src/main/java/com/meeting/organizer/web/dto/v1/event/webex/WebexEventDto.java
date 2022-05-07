package com.meeting.organizer.web.dto.v1.event.webex;

import com.meeting.organizer.client.webex.model.WebexRegistration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebexEventDto {

    private String id;

    private String meetingNumber;

    private String title;

    private String agenda;

    private String password;

    private String timezone;

    private String start;

    private String end;

    private String hostEmail;

    private String siteUrl;

    private String webLink;

    private Boolean enabledAutoRecordMeeting;

    private Boolean enabledJoinBeforeHost;

}
