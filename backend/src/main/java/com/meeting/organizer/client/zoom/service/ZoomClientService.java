package com.meeting.organizer.client.zoom.service;

import com.meeting.organizer.client.zoom.model.ZoomInvitation;
import com.meeting.organizer.client.zoom.model.ZoomMeeting;

public interface ZoomClientService {

    ZoomMeeting createMeeting(ZoomMeeting createEntity);

    void deleteMeeting(Long id);

    ZoomMeeting getById(Long id);

    ZoomMeeting updateMeeting(ZoomMeeting updateEntity);

    ZoomInvitation getMeetingInvitation(Long meetingId);

    Boolean checkZoomUserExistsByEmail(String email);

}
