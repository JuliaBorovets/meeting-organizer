package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.notification.NotificationReadDto;
import com.meeting.organizer.web.dto.v1.notification.NotificationResponse;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    NotificationResponse getNotificationListByUserId(Long userId, Pageable pageable);

    NotificationResponse getReadNotificationListByUserId(Long userId, Pageable pageable);

    NotificationResponse readNotifications(NotificationReadDto readDto);
}
