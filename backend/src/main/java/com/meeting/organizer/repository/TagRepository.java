package com.meeting.organizer.repository;

import com.meeting.organizer.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
