package com.meeting.organizer.exception.custom;

public class MeetingCanNotUpdateException extends RuntimeException {

    public MeetingCanNotUpdateException() {
    }

    public MeetingCanNotUpdateException(String message) {
        super(message);
    }

    public MeetingCanNotUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingCanNotUpdateException(Throwable cause) {
        super(cause);
    }

    public MeetingCanNotUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
