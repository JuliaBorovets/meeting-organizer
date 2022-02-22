package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
