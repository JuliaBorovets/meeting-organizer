package com.meeting.organizer.web.dto.v1.event;

import com.meeting.organizer.model.EventType;
import com.meeting.organizer.model.MeetingType;
import com.meeting.organizer.model.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxNumberParticipants;

    private String name;

    private Byte[] photo;

    private EventType eventType;

    private State state;

//    private List<Tag> tags = new ArrayList<>();
//
//    private Location location;

    private MeetingType meetingType;

    private MeetingDto meetingEntity;

    private Long libraryId;

    private Long streamId;
}