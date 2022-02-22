package com.meeting.organizer.repository;

import com.meeting.organizer.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamRepository extends JpaRepository<Stream, Long> {
}
