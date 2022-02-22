package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
