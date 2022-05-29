package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.VerificationToken;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends PagingAndSortingRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);

}
