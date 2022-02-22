package com.meeting.organizer.service;

import java.util.Optional;

public interface CRUDService<E> {

    E save(E entity);

}
