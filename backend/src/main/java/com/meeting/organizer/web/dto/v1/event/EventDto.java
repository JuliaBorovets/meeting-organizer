package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private Long eventId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxNumberParticipants;

    private String name;

    private Byte[] photo;

    private EventType eventType;

    private State state;

    private List<Tag> tags = new ArrayList<>();

    private Location location;

    private MeetingType meetingType;

    private String externalMeetingId;

    private MeetingDto meetingEntity;

    private Long streamId;
}
