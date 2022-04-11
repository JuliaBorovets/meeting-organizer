package com.meeting.organizer.repository;

import com.meeting.organizer.model.Subscription;
import com.meeting.organizer.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {

    Optional<Subscription> findByUser(User user);

    Optional<Subscription> findByUser_UserId(Long userId);
}
