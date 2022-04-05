package com.meeting.organizer.client.zoom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoomMeetingSettings {

    private boolean allow_multiple_devices;

    // Whether to start meetings with the host video on.
    private boolean host_video;

    // If true, only authenticated users can join the meeting.
    private boolean meeting_authentication;

    // Whether to mute participants upon entry.
    private boolean mute_upon_entry;

    // Whether to start meetings with the participant video on.
    private boolean participant_video;

    // Whether to enable the Waiting Room feature.
    private boolean waiting_room;

}
