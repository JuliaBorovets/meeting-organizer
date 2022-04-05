package com.meeting.organizer.repository.user;

import com.meeting.organizer.model.user.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuthorityRepository extends PagingAndSortingRepository<Authority, Long> {
}
