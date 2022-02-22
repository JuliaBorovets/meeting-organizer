package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
}
