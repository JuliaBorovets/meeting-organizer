package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.model.*;
import com.meeting.organizer.web.dto.v1.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private Long eventId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer maxNumberParticipants;

    private String name;

    private String description;

    private String imagePath;

    private EventType eventType;

    private State state;

    private List<Tag> tags = new ArrayList<>();

    private Location location;

    private MeetingType meetingType;

    private String externalMeetingId;

    private Object meetingEntity;

    private Long streamId;

    private Boolean isFavorite = false;

    private Boolean isPrivate;

    private String joinUrl;

    private Long participantCount;

    private UserDto user;

    private Long durationInMinutes;
}
