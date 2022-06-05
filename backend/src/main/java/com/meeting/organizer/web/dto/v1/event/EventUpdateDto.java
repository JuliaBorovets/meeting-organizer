package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.model.MeetingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUpdateDto {

    private Long eventId;

    private LocalDateTime startDate;

    private Long durationInMinutes;

    private Integer maxNumberParticipants;

    private String name;

    private String description;

    private String imagePath;

    private MeetingType meetingType;

    private Object meetingEntity;

    private Boolean isPrivate;

    private String joinUrl;
}
