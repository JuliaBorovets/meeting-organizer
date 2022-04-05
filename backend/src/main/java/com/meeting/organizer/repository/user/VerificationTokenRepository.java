package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VerificationTokenRepository extends PagingAndSortingRepository<VerificationToken, Long> {
}
