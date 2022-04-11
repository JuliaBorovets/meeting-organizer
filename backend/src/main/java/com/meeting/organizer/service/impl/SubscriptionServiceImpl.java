package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.SubscriptionNotFoundException;
import com.meeting.organizer.model.Subscription;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.SubscriptionRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.SubscriptionService;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionCreateDto;
import com.meeting.organizer.web.dto.v1.subscription.SubscriptionDto;
import com.meeting.organizer.web.mapper.v1.SubscriptionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionServiceImpl extends AbstractService<Subscription, SubscriptionRepository>
        implements SubscriptionService {

    private final CRUDService<User> userService;
    private final SubscriptionMapper mapper;

    public SubscriptionServiceImpl(SubscriptionRepository repository,
                                   @Qualifier("userServiceImpl") CRUDService<User> userService,
                                   SubscriptionMapper mapper) {
        super(repository);
        this.userService = userService;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public SubscriptionDto subscribeToNotification(SubscriptionCreateDto createDto) {
        User user = userService.findById(createDto.getUserId());
        Optional<Subscription> foundSubscription = repository.findByUser(user);

        if (foundSubscription.isPresent()) {
            log.info("Subscription for User with id={} already exists.", user.getUserId());
            return mapper.subscriptionToSubscriptionDto(foundSubscription.get());
        }

        Subscription subscription = Subscription.builder()
                .user(user)
                .build();

        Subscription savedSubscription = repository.save(subscription);

        user.getSubscriptions().add(savedSubscription);
        userService.save(user);

        return mapper.subscriptionToSubscriptionDto(savedSubscription);
    }

    @Override
    public SubscriptionDto getSubscriptionByUserId(Long userId) {
        Subscription subscription = repository.findByUser_UserId(userId)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for user with id=" + userId));
        return mapper.subscriptionToSubscriptionDto(
                subscription
        );
    }
}
