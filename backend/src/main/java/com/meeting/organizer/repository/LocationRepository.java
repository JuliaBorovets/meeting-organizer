package com.meeting.organizer.repository;

import com.meeting.organizer.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
