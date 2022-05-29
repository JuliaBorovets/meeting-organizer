package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByVisitedEvents_EventIdAndUsernameLike(Long eventId, String username, Pageable pageable);

    Long countByVisitedEvents_EventIdAndUsernameLike(Long eventId, String username);

    List<User> findAllByVisitedEvents_EventId(Long eventId);

}
