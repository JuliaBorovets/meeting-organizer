package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private Location location;

    private MeetingType meetingType;

    private MeetingDto meetingEntity;

    private Boolean isPrivate;
}
