package com.meeting.organizer.exception.custom;

public class UnsupportedEventException extends RuntimeException {
    public UnsupportedEventException() {
    }

    public UnsupportedEventException(String message) {
        super(message);
    }

    public UnsupportedEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedEventException(Throwable cause) {
        super(cause);
    }

    public UnsupportedEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
