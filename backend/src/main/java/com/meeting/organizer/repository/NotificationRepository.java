package com.meeting.organizer.repository;

import com.meeting.organizer.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long> {

    List<Notification> findByUser_UserId(Long userId, Pageable pageable);

    List<Notification> findByUser_UserIdAndIsRead(Long userId, Boolean isRead, Pageable pageable);

    Long countByUser_UserId(Long userId);

    Long countByUser_UserIdAndIsRead(Long userId, Boolean isRead);

}
