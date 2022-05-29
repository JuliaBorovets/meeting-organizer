package com.meeting.organizer.service.impl;

import com.meeting.organizer.model.Event;
import com.meeting.organizer.service.ExecutorService;
import com.meeting.organizer.service.MailService;
import com.meeting.organizer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
@Service
public class ExecutorServiceImpl implements ExecutorService {

    private final ScheduledExecutorService executorService;
    private final ConcurrentHashMap<String, ScheduledFuture<?>> tasksMap;
    private final UserService userService;
    private final MailService mailService;

    public ExecutorServiceImpl(MailService mailService, UserService userService) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.mailService = mailService;
        this.tasksMap = new ConcurrentHashMap<>();
        this.userService = userService;
    }

    @Override
    public void scheduleEventNotificationTask(Event event) {
        log.info("Schedule notification task for event with  id={}", event.getEventId());
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime meetingStartTime = event.getStartDate();
        if (localDateTimeNow.isAfter(meetingStartTime)) {
            log.info("meeting start time before now");
            return;
        }

        LocalDateTime notificationLocalDateTime = meetingStartTime.minus(15, ChronoUnit.MINUTES);

        long initialDelay = 5;

        if (localDateTimeNow.isBefore(notificationLocalDateTime)) {
            Duration duration = Duration.between(LocalDateTime.now(), notificationLocalDateTime);
            initialDelay = duration.getSeconds();
        }

        log.info("send notification in {} seconds", initialDelay);

        Runnable task = () -> {
            userService.getEventVisitors(event.getEventId())
                    .forEach(user -> {
                        mailService.sendEventNotificationMail(user, event, Duration.between(LocalDateTime.now(), event.getStartDate()).toMinutes());
                    });
        };
        ScheduledFuture<?> scheduledFuture = executorService.schedule(task, initialDelay, TimeUnit.SECONDS);
        this.tasksMap.put(event.getEventId().toString(), scheduledFuture);
    }

    @Override
    public void cancelEventNotificationTask(Event event) {
        log.info("Cancel notification task for event with  id={}", event.getEventId());
        ScheduledFuture<?> task = tasksMap.get(event.getEventId().toString());
        if (Objects.nonNull(task)) {
            task.cancel(false);
            tasksMap.remove(event.getEventId().toString());
        }
    }
}
