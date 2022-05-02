package com.meeting.organizer.exception.custom;

public class CanNotRefreshTokenException extends RuntimeException {
    public CanNotRefreshTokenException() {
    }

    public CanNotRefreshTokenException(String message) {
        super(message);
    }

    public CanNotRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotRefreshTokenException(Throwable cause) {
        super(cause);
    }

    public CanNotRefreshTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
