package com.meeting.organizer.repository;

import com.meeting.organizer.model.Stream;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StreamRepository extends PagingAndSortingRepository<Stream, Long> {

    List<Stream> findByLibrary_LibraryIdAndNameLike(Long libraryId, String streamName, Pageable pageable);
}
