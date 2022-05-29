package com.meeting.organizer.client.zoom.model;

import com.meeting.organizer.web.dto.v1.event.zoom.ZoomMeetingSettingCreateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoomMeeting {

    // The ID of the user who scheduled this meeting on behalf of the host.
    private String assistant_id;

    // Email address of the meeting host.
    private String host_email;

    // ID of the user who is set as host of meeting.
    private String host_id;

    private Long id;

    private String uuid;

    private String agenda;

    private String created_at;

    private Integer duration;

    private String encrypted_password;

    private String h323_password;

    private String join_url;

    private String password;

    // Personal Meeting Id. Only used for scheduled meetings and recurring meetings with no fixed time.
    private Long pmi;

    private boolean pre_schedule;

    private ZoomMeetingSettingCreateDto settings;

    private String start_time;

    private String start_url;

    //  Enum: "waiting" "started" Meeting status
    private String status;

    private String timezone;

    private String topic;

    private String type;
}
