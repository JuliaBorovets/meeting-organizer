package com.meeting.organizer.exception.custom;

public class MeetingCanNotDeleteException extends RuntimeException {

    public MeetingCanNotDeleteException() {
    }

    public MeetingCanNotDeleteException(String message) {
        super(message);
    }

    public MeetingCanNotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingCanNotDeleteException(Throwable cause) {
        super(cause);
    }

    public MeetingCanNotDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
