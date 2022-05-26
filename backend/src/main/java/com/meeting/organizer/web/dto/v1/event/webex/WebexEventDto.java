package com.meeting.organizer.web.dto.v1.event.webex;

import com.meeting.organizer.web.dto.v1.event.MeetingDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class WebexEventDto extends MeetingDto {

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
