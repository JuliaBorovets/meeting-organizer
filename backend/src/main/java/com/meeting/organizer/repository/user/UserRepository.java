package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByToken_VerificationTokenId(Long tokenId);

}
