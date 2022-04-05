package com.meeting.organizer.service;

public interface CRUDService<E> {

    E save(E entity);

    E findById(Long id);

    void deleteById(Long id);

}
