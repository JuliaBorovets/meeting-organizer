package com.meeting.organizer.repository;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface LibraryRepository extends PagingAndSortingRepository<Library, Long> {

    List<Library> findByUser_UserId(Long userId, Pageable pageable);

    Long countByUser_UserId(Long userId);

}
