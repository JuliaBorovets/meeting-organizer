package com.meeting.organizer.client.webex.service;

import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;

public interface WebexClientService {

    WebexMeeting createMeeting(WebexCreateMeeting createEntity);

    void deleteMeeting(Long id);

    WebexMeeting getById(String id);
}
