package com.meeting.organizer.repository;

import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.Stream;
import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findByLibrary_LibraryId(Long libraryId, Pageable pageable);

    List<Event> findByLibrary_LibraryIdAndStream_StreamId(Long libraryId, Long streamId, Pageable pageable);

    List<Event> findByLibrary_LibraryIdNotContaining(Long libraryId, Pageable pageable);

    Long countByLibrary_LibraryId(Long libraryId);

    List<Event> findByLibrary_LibraryIdAndStream_StreamIdAndNameLike(Long libraryId, Long stream, String name,  Pageable pageable);

    List<Event> findEventsByUsersFavorite_UserId(Long userId, Pageable pageable);

    List<Event> findEventsByIsPrivateOrGivenAccessListContainsOrUser(Boolean isPrivate, User user, User creator, Pageable pageable);

    List<Event> findAllByUser_UserId(Long userId, Pageable pageable);

    List<Event> findEventsByGivenAccessList_UserId(Long userId, Pageable pageable);

    Optional<Event> findByAccessToken(String accessToken);
}
