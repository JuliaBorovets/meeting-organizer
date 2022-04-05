package com.meeting.organizer.exception.custom;

public class MeetingNotFoundException extends RuntimeException {

    public MeetingNotFoundException() {
    }

    public MeetingNotFoundException(String message) {
        super(message);
    }

    public MeetingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingNotFoundException(Throwable cause) {
        super(cause);
    }

    public MeetingNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
