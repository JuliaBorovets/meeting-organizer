package com.meeting.organizer.service;

import org.springframework.data.jpa.repository.JpaRepository;


public abstract class AbstractService<E, R extends JpaRepository<E, Long>> implements CRUDService<E> {

    protected final R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

}
