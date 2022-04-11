package com.meeting.organizer.service.impl;

import com.meeting.organizer.model.Notification;
import com.meeting.organizer.repository.NotificationRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.NotificationService;
import com.meeting.organizer.web.dto.v1.notification.NotificationDto;
import com.meeting.organizer.web.dto.v1.notification.NotificationReadDto;
import com.meeting.organizer.web.dto.v1.notification.NotificationResponse;
import com.meeting.organizer.web.mapper.v1.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class NotificationServiceImpl extends AbstractService<Notification, NotificationRepository>
        implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository repository,
                                   NotificationMapper notificationMapper) {
        super(repository);
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationResponse getNotificationListByUserId(Long userId, Pageable pageable) {

        NotificationResponse response = new NotificationResponse();

        List<NotificationDto> notificationDtoList = repository.findByUser_UserId(userId, pageable)
                .stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());

        response.setList(notificationDtoList);
        response.setTotalItems(countAllByUserId(userId));

        return response;
    }

    @Override
    public NotificationResponse getReadNotificationListByUserId(Long userId, Pageable pageable) {
        NotificationResponse response = new NotificationResponse();

        List<NotificationDto> notificationDtoList = repository.findByUser_UserIdAndIsRead(userId, false, pageable)
                .stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());

        response.setList(notificationDtoList);
        response.setTotalItems(countAllReadByUserId(userId));

        return response;
    }

    @Override
    public NotificationResponse readNotifications(NotificationReadDto readDto) {
        List<Long> notificationIdList = readDto.getNotificationDtoList().stream()
                .map(NotificationDto::getNotificationId)
                .collect(Collectors.toList());

        List<Notification> notifications = StreamSupport.stream(repository.findAllById(notificationIdList).spliterator(), false)
                .collect(Collectors.toList());
        notifications.forEach(n -> n.setIsRead(Boolean.TRUE));
        repository.saveAll(notifications);

        List<NotificationDto> notificationDtoList = notifications.stream()
                .map(notificationMapper::notificationToNotificationDto)
                .collect(Collectors.toList());

        NotificationResponse response = new NotificationResponse();
        response.setTotalItems((long) notifications.size());
        response.setList(notificationDtoList);
        return response;
    }

    private Long countAllByUserId(Long userId) {
        return repository.countByUser_UserId(userId);
    }

    private Long countAllReadByUserId(Long userId) {
        return repository.countByUser_UserIdAndIsRead(userId, false);
    }
}
