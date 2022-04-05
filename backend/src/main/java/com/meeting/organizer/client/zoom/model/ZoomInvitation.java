package com.meeting.organizer.client.zoom.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZoomInvitation {

    // Meeting invitation.
    private String invitation;

    // A list of SIP phone addresses.
    private List<String> sip_links = new ArrayList<>();
}
