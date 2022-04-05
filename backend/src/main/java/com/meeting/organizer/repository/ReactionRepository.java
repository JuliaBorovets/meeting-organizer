package com.meeting.organizer.repository;

import com.meeting.organizer.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReactionRepository extends PagingAndSortingRepository<Reaction, Long> {
}
