package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.utils.BaseUIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EventResponse extends BaseUIResponse<EventDto> {
}
