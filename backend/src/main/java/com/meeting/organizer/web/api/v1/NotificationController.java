package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.NotificationService;
import com.meeting.organizer.web.dto.v1.notification.NotificationReadDto;
import com.meeting.organizer.web.dto.v1.notification.NotificationResponse;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(NotificationController.BASE_URL)
@RequiredArgsConstructor
public class NotificationController {

    public static final String BASE_URL = "/api/v1/notification";
    private final NotificationService notificationService;

    @GetMapping
    public NotificationResponse getNotificationList(@RequestParam(value = "userId", required = false) Long userId,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return notificationService.getNotificationListByUserId(userId, pageable);
    }

    @GetMapping("/read")
    public NotificationResponse getReadNotificationList(@RequestParam(value = "userId", required = false) Long userId,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return notificationService.getReadNotificationListByUserId(userId, pageable);
    }

    @PutMapping
    public NotificationResponse readNotifications(@RequestBody NotificationReadDto readDto) {
        return notificationService.readNotifications(readDto);
    }
}
