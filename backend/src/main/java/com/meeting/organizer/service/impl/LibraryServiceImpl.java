package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.LibraryNotFoundException;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.LibraryRepository;
import com.meeting.organizer.service.*;
import com.meeting.organizer.web.dto.v1.library.*;
import com.meeting.organizer.web.mapper.v1.LibraryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LibraryServiceImpl extends AbstractService<Library, LibraryRepository> implements LibraryService {

    private final LibraryMapper libraryMapper;
    private final CRUDService<User> userCRUDService;
    private final UserService userService;
    private final MailService mailService;


    public LibraryServiceImpl(LibraryRepository libraryRepository,
                              LibraryMapper libraryMapper,
                              @Qualifier("userServiceImpl") CRUDService<User> userCRUDService,
                              UserService userService,
                              MailService mailService
    ) {
        super(libraryRepository);
        this.libraryMapper = libraryMapper;
        this.userCRUDService = userCRUDService;
        this.userService = userService;
        this.mailService = mailService;
    }

    @Transactional
    @Override
    public LibraryDto createLibrary(LibraryCreateDto libraryDto) {
        log.info("create library: {}", libraryDto);
        Library convertedEntity = libraryMapper.libraryCreateDtoToLibrary(libraryDto);
        User user = userCRUDService.findById(libraryDto.getUserId());
        convertedEntity.setUser(user);
        convertedEntity.setAccessToken(UUID.randomUUID().toString());

        Library createdLibrary = repository.save(convertedEntity);
        user.getLibraries().add(createdLibrary);
        userCRUDService.save(user);

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
        updatedLibrary.setIsPrivate(libraryDto.getIsPrivate());
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
        return convertToDto(
                findById(id)
        );
    }

    @Override
    public LibraryResponse getUserLibraryListPaginated(Long userId, Pageable pageable) {
        LibraryResponse response = new LibraryResponse();

        List<LibraryDto> libraryDtoList = repository.findByUser_UserId(userId, pageable)
                .stream()
                .map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        response.setList(libraryDtoList);
        response.setTotalItems(calculateTotalUserLibraryCount(userId));

        return response;
    }

    @Override
    public LibraryResponse getLibraryListPaginated(Long userId, Pageable pageable) {
        LibraryResponse response = new LibraryResponse();
        User user = userCRUDService.findById(userId);

        List<LibraryDto> libraryDtoList = repository.findLibrariesByIsPrivateOrGivenAccessListContainsOrUser(false, user, user, pageable)
                .stream()
                .map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        response.setList(libraryDtoList);
        response.setTotalItems(calculateTotalLibraryCount());
        return response;
    }

    @Override
    public LibraryDto addLibraryToFavorites(Long libraryId, Long userId) {
        Library library = findById(libraryId);
        User user = userCRUDService.findById(userId);

        library.getUsersFavorite().add(user);
        user.getFavoriteLibraries().add(library);
        repository.save(library);
        userCRUDService.save(user);

        return convertToDto(library, userId);
    }

    @Override
    public LibraryDto removeLibraryFromFavorites(Long libraryId, Long userId) {
        Library library = findById(libraryId);
        User user = userCRUDService.findById(userId);

        library.getUsersFavorite().remove(user);
        user.getFavoriteLibraries().remove(library);
        repository.save(library);
        userCRUDService.save(user);

        return convertToDto(library, userId);
    }

    @Override
    public LibraryResponse getUserFavoriteLibrariesPaginated(Long userId, Pageable pageable) {
        List<LibraryDto> libraryDtoList = repository.findLibrariesByUsersFavorite_UserId(userId, pageable).stream()
                .map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        LibraryResponse libraryResponse = new LibraryResponse();
        libraryResponse.setTotalItems((long) libraryDtoList.size());
        libraryResponse.setList(libraryDtoList);

        return libraryResponse;
    }

    @Override
    public LibraryDto addAccessToLibraryByUserEmail(AddLibraryAccessDto addLibraryAccessDto) {
        Library library = findById(addLibraryAccessDto.getLibraryId());

        List<User> users = addLibraryAccessDto.getEmailList().stream()
                .map(userService::findByEmail)
                .collect(Collectors.toList());

        library.getGivenAccessList().addAll(users);
        users.forEach(u -> {
            u.getGivenAccessLibraries().add(library);
            userCRUDService.save(u);
            mailService.sendAddLibraryAccessMail(u, library);
        });

        repository.save(library);

        return convertToDto(library);
    }

    @Override
    public LibraryDto removeAccessToLibraryByUserEmail(List<String> emailList, Long libraryId) {
        Library library = findById(libraryId);

        List<User> users = emailList.stream()
                .map(userService::findByEmail)
                .collect(Collectors.toList());

        library.getGivenAccessList()
                .removeAll(users);
        users.forEach(u -> {
            u.getGivenAccessLibraries().remove(library);
            userCRUDService.save(u);
            mailService.sendRemoveLibraryAccessMail(u, library);
        });

        repository.save(library);

        return convertToDto(library);
    }

    @Override
    public LibraryDto addAccessToLibraryByToken(AddLibraryAccessByTokenDto addLibraryAccessDto) {
        Library library = repository.findByAccessToken(addLibraryAccessDto.getAccessToken())
                .orElseThrow(() -> new LibraryNotFoundException("Cannot find library by token=" + addLibraryAccessDto.getAccessToken()));

        User user = userCRUDService.findById(addLibraryAccessDto.getUserId());

        library.getGivenAccessList().add(user);
        user.getGivenAccessLibraries().add(library);

        userCRUDService.save(user);
        repository.save(library);

        mailService.sendAddLibraryAccessMail(user, library);

        return convertToDto(library);
    }

    @Override
    public LibraryResponse getLibraryGivenAccessListByUser(Long userId, Pageable pageable) {
        List<LibraryDto> libraryDtoList = repository.findLibrariesByGivenAccessList_UserId(userId, pageable)
                .stream().map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        LibraryResponse libraryResponse = new LibraryResponse();
        libraryResponse.setList(libraryDtoList);
        libraryResponse.setTotalItems((long) libraryDtoList.size());

        return libraryResponse;
    }

    @Override
    public Library findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id=" + id));
    }

    private Long calculateTotalUserLibraryCount(Long userId) {
        return repository.countByUser_UserId(userId);
    }

    private Long calculateTotalLibraryCount() {
        return repository.count();
    }

    private LibraryDto convertToDto(Library library, Long userId) {
        Boolean isFavorite = library.getUsersFavorite().stream()
                .anyMatch(u -> userId.equals(u.getUserId()));

        LibraryDto libraryDto = libraryMapper.libraryToLibraryDto(library);
        libraryDto.setIsFavorite(isFavorite);
        return libraryDto;
    }

    private LibraryDto convertToDto(Library library) {
        return libraryMapper.libraryToLibraryDto(library);
    }
}
