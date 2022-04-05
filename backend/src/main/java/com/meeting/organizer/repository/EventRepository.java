package com.meeting.organizer.repository;

import com.meeting.organizer.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findByLibrary_LibraryId(Long libraryId, Pageable pageable);

    List<Event> findByLibrary_LibraryIdAndStream_StreamId(Long libraryId, Long streamId, Pageable pageable);

    Long countByLibrary_LibraryId(Long libraryId);
}
