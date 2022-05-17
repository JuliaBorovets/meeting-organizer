package com.meeting.organizer.repository;

import com.meeting.organizer.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RatingRepository extends PagingAndSortingRepository<Rating, Long> {
}
