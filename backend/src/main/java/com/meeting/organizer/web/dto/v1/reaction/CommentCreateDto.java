package com.meeting.organizer.web.dto.v1.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCreateDto {

    private Long userId;

    private Long eventId;

    private String text;

}
