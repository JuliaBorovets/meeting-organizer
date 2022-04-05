package com.meeting.organizer.web.dto.v1.library;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryCreateDto {

    private String name;

    private String description;

    private Byte[] image;

    private Long userId;
}
