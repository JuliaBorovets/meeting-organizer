package com.meeting.organizer.repository;

import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Stream;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findByLibrary_LibraryId(Long libraryId, Pageable pageable);

    List<Event> findByLibrary_LibraryIdAndStream_StreamId(Long libraryId, Long streamId, Pageable pageable);

    List<Event> findByLibrary_LibraryIdNotContaining(Long libraryId, Pageable pageable);

    Long countByLibrary_LibraryId(Long libraryId);

    List<Event> findByLibrary_LibraryIdAndStream_StreamIdNotAndNameIsStartingWith(Long libraryId, Long streamId, String name, Pageable pageable);

//    List<Event> findByLibrary_LibraryIdAndStream_StreamId(Long libraryId, Long streamId, Pageable pageable);

    List<Event> findByLibrary_LibraryIdAndStream_StreamIdAndNameLike(Long libraryId, Long stream, String name,  Pageable pageable);

}
