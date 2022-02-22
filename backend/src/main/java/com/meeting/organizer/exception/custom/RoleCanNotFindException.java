package com.meeting.organizer.exception.custom;

public class RoleCanNotFindException extends RuntimeException {

    public RoleCanNotFindException() {

    }

    public RoleCanNotFindException(String message) {
        super(message);
    }

}
