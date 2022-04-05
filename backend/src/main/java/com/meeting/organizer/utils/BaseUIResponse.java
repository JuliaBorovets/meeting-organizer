package com.meeting.organizer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUIResponse<T> {

    private List<T> list;

    private Long totalItems;
}