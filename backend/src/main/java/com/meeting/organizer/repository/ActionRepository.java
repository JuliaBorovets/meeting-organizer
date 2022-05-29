package com.meeting.organizer.repository;

import com.meeting.organizer.model.Action;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActionRepository extends PagingAndSortingRepository<Action, Long> {
}
