package com.meeting.organizer.exception.custom;

public class DisabledUserException extends RuntimeException {

    public DisabledUserException() {
        super();
    }

    public DisabledUserException(String message) {
        super(message);
    }
}