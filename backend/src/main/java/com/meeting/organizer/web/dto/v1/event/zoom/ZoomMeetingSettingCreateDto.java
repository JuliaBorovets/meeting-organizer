package com.meeting.organizer.web.dto.v1.event.zoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoomMeetingSettingCreateDto {

    @JsonProperty("allow_multiple_devices")
    private boolean allowMultipleDevices;

    // Whether to start meetings with the host video on.
    @JsonProperty("host_video")
    private boolean hostVideo;

    // If true, only authenticated users can join the meeting.
    @JsonProperty("meeting_authentication")
    private boolean meetingAuthentication;

    // Whether to mute participants upon entry.
    @JsonProperty("mute_upon_entry")
    private boolean muteUponEntry;

    // Whether to start meetings with the participant video on.
    @JsonProperty("participant_video")
    private boolean participantVideo;

    // Whether to enable the Waiting Room feature.
    @JsonProperty("waiting_room")
    private boolean waitingRoom;
}
