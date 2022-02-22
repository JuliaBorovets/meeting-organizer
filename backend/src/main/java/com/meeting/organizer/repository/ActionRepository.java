package com.meeting.organizer.repository;

import com.meeting.organizer.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}
