package com.meeting.organizer.web.dto.v1.event.zoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.meeting.organizer.web.dto.v1.event.MeetingDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ZoomEventUpdateDto extends MeetingDto {

    private String agenda;

    private String password;

    private ZoomMeetingSettingCreateDto settings;

    @JsonProperty("start_time")
    private String start_time;

    private String timezone;

    private String topic;

}
