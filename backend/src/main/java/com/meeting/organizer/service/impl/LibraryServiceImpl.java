package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.LibraryNotFoundException;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.LibraryRepository;
import com.meeting.organizer.service.AbstractService;
import com.meeting.organizer.service.CRUDService;
import com.meeting.organizer.service.LibraryService;
import com.meeting.organizer.web.dto.v1.library.LibraryCreateDto;
import com.meeting.organizer.web.dto.v1.library.LibraryDto;
import com.meeting.organizer.web.dto.v1.library.LibraryResponse;
import com.meeting.organizer.web.dto.v1.library.LibraryUpdateDto;
import com.meeting.organizer.web.mapper.v1.LibraryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LibraryServiceImpl extends AbstractService<Library, LibraryRepository> implements LibraryService {

    private final LibraryMapper libraryMapper;
    private final CRUDService<User> userService;

    public LibraryServiceImpl(LibraryRepository libraryRepository,
                              LibraryMapper libraryMapper,
                              @Qualifier("userServiceImpl") CRUDService<User> userService) {
        super(libraryRepository);
        this.libraryMapper = libraryMapper;
        this.userService = userService;
    }

    @Transactional
    @Override
    public LibraryDto createLibrary(LibraryCreateDto libraryDto) {
        log.info("create library: {}", libraryDto);
        Library convertedEntity = libraryMapper.libraryCreateDtoToLibrary(libraryDto);
        User user = userService.findById(libraryDto.getUserId());
        convertedEntity.setUser(user);

        Library createdLibrary = repository.save(convertedEntity);
        user.getLibraries().add(createdLibrary);
        userService.save(user);

        log.info("created library: {}", createdLibrary);
        return libraryMapper.libraryToLibraryDto(createdLibrary);
    }

    @Transactional
    @Override
    public LibraryDto updateLibrary(LibraryUpdateDto libraryDto) {

        Library updatedLibrary = findById(libraryDto.getLibraryId());
        updatedLibrary.setDescription(libraryDto.getDescription());
        updatedLibrary.setImage(libraryDto.getImage());
        updatedLibrary.setName(libraryDto.getName());
        repository.save(updatedLibrary);

        return libraryMapper.libraryToLibraryDto(updatedLibrary);
    }

    //todo add on delete to model relations to delete all streams and events
    @Transactional
    @Override
    public void deleteLibrary(Long id) {

        log.info("deleting library with id={}", id);
        super.deleteById(id);
    }

    @Override
    public LibraryDto getLibraryDtoById(Long id) {
        return libraryMapper.libraryToLibraryDto(
                findById(id)
        );
    }

    @Override
    public LibraryResponse getLibraryListPaginated(Long userId, Pageable pageable) {
        LibraryResponse response = new LibraryResponse();

        List<LibraryDto> libraryDtoList = repository.findByUser_UserId(userId, pageable)
                .stream()
                .map(libraryMapper::libraryToLibraryDto)
                .collect(Collectors.toList());

        response.setList(libraryDtoList);
        response.setTotalItems(calculateTotalLibraryCount(userId));

        return response;
    }

    @Override
    public Library findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id=" + id));
    }

    private Long calculateTotalLibraryCount(Long userId) {
        return repository.countByUser_UserId(userId);
    }
}
