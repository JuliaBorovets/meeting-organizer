package com.meeting.organizer.service;

import com.meeting.organizer.exception.custom.GeneralNotFoundException;
import org.springframework.data.repository.PagingAndSortingRepository;


public abstract class AbstractService<E, R extends PagingAndSortingRepository<E, Long>> implements CRUDService<E> {

    protected final R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public E save(E entity) {
        return repository.save(entity);
    }

    @Override
    public E findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GeneralNotFoundException("Not found. Id=" + id));
    }

    @Override
    public void deleteById(Long id) {
        E foundEntity = findById(id);
        repository.deleteById(id);
    }

}
