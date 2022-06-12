package com.meeting.organizer.repository;

import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends PagingAndSortingRepository<Event, Long> {

    List<Event> findByLibrary_LibraryIdAndStream_StreamIdAndNameLikeOrderByCreationDateDesc(Long libraryId, Long streamId, String eventName, Pageable pageable);

    Long countByLibrary_LibraryIdAndStream_StreamIdAndNameLike(Long libraryId, Long streamId, String eventName);

    List<Event> findByLibrary_LibraryIdNotContainingAndNameLikeOrderByCreationDateDesc(Long libraryId, String eventName, Pageable pageable);

    Long countByLibrary_LibraryIdNotContainingAndNameLike(Long libraryId, String eventName);

    List<Event> findEventsByUsersFavorite_UserIdAndNameLikeOrderByCreationDateDesc(Long userId, String eventName, Pageable pageable);

    Long countByUsersFavorite_UserIdAndNameLike(Long userId, String eventName);

    List<Event> findEventsByNameLikeAndIsPrivateOrGivenAccessListContainsAndNameLikeOrUserAndNameLikeOrderByCreationDateDesc(String eventName, Boolean isPrivate, User user, String eventName2, User creator, String eventName3,Pageable pageable);

    Long countByNameLikeAndIsPrivateOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(String eventName, Boolean isPrivate, User user, String eventName2, User creator, String eventName3);

    List<Event> findAllByUser_UserIdAndNameLikeOrderByCreationDateDesc(Long userId, String eventName, Pageable pageable);

    Long countByUser_UserIdAndNameLike(Long userId, String eventName);

    List<Event> findEventsByGivenAccessList_UserIdAndNameLikeOrderByCreationDateDesc(Long userId, String eventName, Pageable pageable);

    Long countByGivenAccessList_UserIdAndNameLike(Long userId, String eventName);

    Optional<Event> findByAccessToken(String accessToken);

    List<Event> findByLibrary_LibraryIdAndNameLikeAndUser_UserIdOrderByCreationDateDesc(Long libraryId, String eventName, Long userId, Pageable pageable);
}
