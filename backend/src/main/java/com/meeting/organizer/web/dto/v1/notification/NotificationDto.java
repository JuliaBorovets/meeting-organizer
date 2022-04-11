package com.meeting.organizer.web.dto.v1.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long notificationId;

    private String text;

    private Boolean isRead;
}
