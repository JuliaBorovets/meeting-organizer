package com.meeting.organizer.web.dto.v1.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreamUpdateDto {

    private Long streamId;

    private String name;

}
