package com.meeting.organizer.repository;

import com.meeting.organizer.model.Location;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LocationRepository extends PagingAndSortingRepository<Location, Long> {
}
