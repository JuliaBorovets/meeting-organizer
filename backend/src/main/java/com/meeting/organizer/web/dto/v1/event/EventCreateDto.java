package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.model.EventType;
import com.meeting.organizer.model.MeetingType;
import com.meeting.organizer.model.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateDto {

    private LocalDateTime startDate;

    private Long durationInMinutes;

    private Integer maxNumberParticipants;

    private String name;

    private String description;

    private String imagePath;

    private EventType eventType;

    private State state;

    private MeetingType meetingType;

    private Object meetingEntity;

    private Long libraryId;

    private Long streamId;

    private Boolean generateMeeting;

    private String joinUrl;

    private Boolean isPrivate;

    private Long userId;
}
