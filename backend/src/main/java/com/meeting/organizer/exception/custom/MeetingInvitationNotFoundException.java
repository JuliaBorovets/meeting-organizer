package com.meeting.organizer.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MeetingInvitationNotFoundException extends RuntimeException {

    public MeetingInvitationNotFoundException() {
    }

    public MeetingInvitationNotFoundException(String message) {
        super(message);
    }

    public MeetingInvitationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingInvitationNotFoundException(Throwable cause) {
        super(cause);
    }

    public MeetingInvitationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
