package com.meeting.organizer.client.webex.service;

import com.meeting.organizer.client.webex.model.WebexMeeting;

public interface WebexClientService {

    WebexMeeting createMeeting(WebexMeeting createEntity);

    void deleteMeeting(Long id);
}
