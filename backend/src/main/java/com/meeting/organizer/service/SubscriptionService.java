package com.meeting.organizer.service;

import com.meeting.organizer.web.dto.v1.subscription.SubscriptionCreateDto;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionDto;

public interface SubscriptionService {

    SubscriptionDto subscribeToNotification(SubscriptionCreateDto createDto);

    SubscriptionDto getSubscriptionByUserId(Long userId);
}
