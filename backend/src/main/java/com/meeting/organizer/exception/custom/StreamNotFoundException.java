package com.meeting.organizer.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StreamNotFoundException extends RuntimeException {

    public StreamNotFoundException() {
    }

    public StreamNotFoundException(String message) {
        super(message);
    }

    public StreamNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamNotFoundException(Throwable cause) {
        super(cause);
    }

    public StreamNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
