package com.meeting.organizer.client.webex.service;

import com.meeting.organizer.client.webex.model.WebexCreateMeeting;
import com.meeting.organizer.client.webex.model.WebexMeeting;
import com.meeting.organizer.client.webex.model.WebexUpdateMeeting;

public interface WebexClientService {

    WebexMeeting createMeeting(WebexCreateMeeting createEntity);

    WebexMeeting updateMeeting(String id, WebexUpdateMeeting updateMeeting);

    void deleteMeeting(String id);

    WebexMeeting getById(String id);
}
