package com.meeting.organizer.service;

import com.meeting.organizer.model.Event;

public interface ExecutorService {

    void scheduleEventNotificationTask(Event event);

    void cancelEventNotificationTask(Event event);

}
