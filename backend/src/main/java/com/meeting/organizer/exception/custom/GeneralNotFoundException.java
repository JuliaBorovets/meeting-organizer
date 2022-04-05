package com.meeting.organizer.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GeneralNotFoundException extends RuntimeException {


    public GeneralNotFoundException() {
    }

    public GeneralNotFoundException(String message) {
        super(message);
    }

    public GeneralNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneralNotFoundException(Throwable cause) {
        super(cause);
    }

    public GeneralNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
