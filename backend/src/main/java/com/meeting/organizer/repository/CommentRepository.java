package com.meeting.organizer.repository;

import com.meeting.organizer.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    List<Comment> findAllByEvent_EventIdOrderByCreationDateDesc(Long eventId, Pageable pageable);

    Long countAllByEvent_EventIdOrderByCreationDateDesc(Long eventId);
}
