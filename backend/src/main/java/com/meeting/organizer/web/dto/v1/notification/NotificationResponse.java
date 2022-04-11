package com.meeting.organizer.web.dto.v1.notification;

import com.meeting.organizer.utils.BaseUIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class NotificationResponse extends BaseUIResponse<NotificationDto> {
}
