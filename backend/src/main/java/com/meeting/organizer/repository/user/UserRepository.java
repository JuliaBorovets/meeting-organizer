package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
