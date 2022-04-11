package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Subscription;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionDto subscriptionToSubscriptionDto(Subscription subscription);

}
