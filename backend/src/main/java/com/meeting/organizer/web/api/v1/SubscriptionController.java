package com.meeting.organizer.web.api.v1;

import com.meeting.organizer.service.SubscriptionService;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionCreateDto;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(SubscriptionController.BASE_URL)
@RequiredArgsConstructor
public class SubscriptionController {

    public static final String BASE_URL = "/api/v1/subscription";
    private final SubscriptionService subscriptionService;

    @PostMapping
    public SubscriptionDto subscribeToNotifications(@RequestBody SubscriptionCreateDto createDto) {
        return subscriptionService.subscribeToNotification(createDto);
    }

    @GetMapping("/{userId}")
    public SubscriptionDto subscribeToNotifications(@PathVariable Long userId) {
        return subscriptionService.getSubscriptionByUserId(userId);
    }
}
