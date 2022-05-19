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

    List<Event> findByLibrary_LibraryIdAndStream_StreamIdAndNameLike(Long libraryId, Long streamId, String eventName, Pageable pageable);

    List<Event> findByLibrary_LibraryIdNotContainingAndNameLike(Long libraryId, String eventName, Pageable pageable);

    Long countByLibrary_LibraryId(Long libraryId);

    List<Event> findEventsByUsersFavorite_UserIdAndNameLike(Long userId, String eventName, Pageable pageable);

    List<Event> findEventsByNameLikeAndIsPrivateOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(String eventName, Boolean isPrivate, User user, String eventName2, User creator, String eventName3,Pageable pageable);

    List<Event> findAllByUser_UserIdAndNameLike(Long userId, String eventName,Pageable pageable);

    List<Event> findEventsByGivenAccessList_UserIdAndNameLike(Long userId, String eventName, Pageable pageable);

    Optional<Event> findByAccessToken(String accessToken);
}
