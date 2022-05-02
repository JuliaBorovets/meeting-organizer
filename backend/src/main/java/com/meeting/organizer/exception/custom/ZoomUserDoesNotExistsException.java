package com.meeting.organizer.exception.custom;

public class ZoomUserDoesNotExistsException extends RuntimeException {
    public ZoomUserDoesNotExistsException() {
    }

    public ZoomUserDoesNotExistsException(String message) {
        super(message);
    }

    public ZoomUserDoesNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZoomUserDoesNotExistsException(Throwable cause) {
        super(cause);
    }

    public ZoomUserDoesNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
