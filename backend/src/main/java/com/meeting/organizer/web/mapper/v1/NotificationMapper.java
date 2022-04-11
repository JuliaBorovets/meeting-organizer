package com.meeting.organizer.web.mapper.v1;

import com.meeting.organizer.model.Notification;
import com.meeting.organizer.web.dto.v1.notification.NotificationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDto notificationToNotificationDto(Notification notification);

}
