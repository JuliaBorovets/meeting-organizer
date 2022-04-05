package com.meeting.organizer.exception.custom;

public class MeetingCanNotCreateException extends RuntimeException {

    public MeetingCanNotCreateException() {
    }

    public MeetingCanNotCreateException(String message) {
        super(message);
    }

    public MeetingCanNotCreateException(Throwable cause) {
        super(cause);
    }
}
