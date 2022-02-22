package com.meeting.organizer.repository;

import com.meeting.organizer.model.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
