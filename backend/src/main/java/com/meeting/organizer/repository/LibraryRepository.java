package com.meeting.organizer.repository;

import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface LibraryRepository extends PagingAndSortingRepository<Library, Long> {

    List<Library> findByUser_UserIdAndNameLike(Long userId, String libraryName, Pageable pageable);

    Long countByUser_UserIdAndNameLike(Long userId, String libraryName);

    List<Library> findLibrariesByUsersFavorite_UserIdAndNameLike(Long userId, String libraryName, Pageable pageable);

    List<Library> findLibrariesByIsPrivateAndNameLikeOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(Boolean isPrivate, String libraryName, User user, String libraryName1, User creator, String libraryName2, Pageable pageable);

    Optional<Library> findByAccessToken(String accessToken);

    List<Library> findLibrariesByGivenAccessList_UserIdAndNameLike(Long userId, String libraryName, Pageable pageable);
}
