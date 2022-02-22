package com.meeting.organizer.repository;

import com.meeting.organizer.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
