package com.meeting.organizer.repository;

import com.meeting.organizer.model.Rating;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
}
