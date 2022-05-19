package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByToken_VerificationTokenId(Long tokenId);

    List<User> findAllByVisitedEvents_EventIdAndUsernameLike(Long eventId, String username, Pageable pageable);

    Long countAllByVisitedEvents_EventId(Long eventId);

}
