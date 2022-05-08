package com.meeting.organizer.repository;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends PagingAndSortingRepository<Library, Long> {

    List<Library> findByUser_UserId(Long userId, Pageable pageable);

    Long countByUser_UserId(Long userId);

    List<Library> findLibrariesByUsersFavorite_UserId(Long userId, Pageable pageable);

    List<Library> findLibrariesByIsPrivateOrGivenAccessListContainsOrUser(Boolean isPrivate, User user, User creator, Pageable pageable);

    Optional<Library> findByAccessToken(String accessToken);

    List<Library> findLibrariesByGivenAccessList_UserId(Long userId, Pageable pageable);
}
