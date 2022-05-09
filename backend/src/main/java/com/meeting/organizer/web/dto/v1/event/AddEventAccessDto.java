package com.meeting.organizer.web.dto.v1.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEventAccessDto {

    List<String> emailList = new ArrayList<>();

    Long eventId;
}
