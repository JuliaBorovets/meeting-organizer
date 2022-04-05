package com.meeting.organizer.web.dto.v1.event.zoom;

import com.meeting.organizer.web.dto.v1.event.MeetingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ZoomEventDto extends MeetingDto {

    // Email address of the meeting host.
    private String host_email;

    private String uuid;

    private String agenda;

    private String created_at;

    private Integer duration;

    private String join_url;

    private String password;

    private ZoomMeetingSettingCreateDto settings;

    private String start_time;

    private String start_url;

    //  Enum: "waiting" "started" Meeting status
    private String status;

    private String timezone;

    private String topic;

}
