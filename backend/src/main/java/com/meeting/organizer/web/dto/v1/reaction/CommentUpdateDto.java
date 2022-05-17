package com.meeting.organizer.web.dto.v1.reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentUpdateDto {

    private Long commentId;

    private String text;

}
