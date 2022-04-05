package com.meeting.organizer.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleCanNotFindException extends RuntimeException {

    public RoleCanNotFindException() {

    }

    public RoleCanNotFindException(String message) {
        super(message);
    }

}
