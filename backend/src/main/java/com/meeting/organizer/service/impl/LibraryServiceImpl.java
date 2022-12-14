package com.meeting.organizer.service.impl;

import com.meeting.organizer.exception.custom.LibraryNotFoundException;
import com.meeting.organizer.model.Event;
import com.meeting.organizer.model.Library;
import com.meeting.organizer.model.user.User;
import com.meeting.organizer.repository.LibraryRepository;
import com.meeting.organizer.service.*;
import com.meeting.organizer.web.dto.v1.library.*;
import com.meeting.organizer.web.mapper.v1.LibraryMapper;
import lombok.extern.slf4j.Slf4j;
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
    private final UserService userService;
    private final MailService mailService;
    private final EventService eventService;

    public LibraryServiceImpl(LibraryRepository libraryRepository,
                              LibraryMapper libraryMapper,
                              UserService userService,
                              EventService eventService,
                              MailService mailService
    ) {
        super(libraryRepository);
        this.libraryMapper = libraryMapper;
        this.userService = userService;
        this.mailService = mailService;
        this.eventService = eventService;
    }

    @Transactional
    @Override
    public LibraryDto createLibrary(LibraryCreateDto libraryDto) {
        log.info("create library: {}", libraryDto);
        Library convertedEntity = libraryMapper.libraryCreateDtoToLibrary(libraryDto);
        User user = userService.findById(libraryDto.getUserId());
        convertedEntity.setUser(user);
        convertedEntity.setAccessToken(UUID.randomUUID().toString());

        Library createdLibrary = repository.save(convertedEntity);
        user.getLibraries().add(createdLibrary);

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

    @Override
    public LibraryDto getLibraryDtoById(Long id) {
        return convertToDto(
                findById(id)
        );
    }

    @Override
    public LibraryResponse getUserLibraryListPaginated(Long userId, String libraryName, Pageable pageable) {
        LibraryResponse response = new LibraryResponse();

        String libraryNamePattern = libraryName + "%";

        List<LibraryDto> libraryDtoList = repository.findByUser_UserIdAndNameLike(userId, libraryNamePattern, pageable)
                .stream()
                .map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        response.setList(libraryDtoList);
        response.setTotalItems(repository.countByUser_UserIdAndNameLike(userId, libraryNamePattern));

        return response;
    }

    @Override
    public LibraryResponse getLibraryListPaginated(Long userId, String libraryName, Pageable pageable) {
        LibraryResponse response = new LibraryResponse();
        User user = userService.findById(userId);
        String libraryNamePattern = libraryName + "%";

        List<LibraryDto> libraryDtoList = repository.findLibrariesByIsPrivateAndNameLikeOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(false, libraryNamePattern, user, libraryNamePattern, user, libraryNamePattern, pageable)
                .stream()
                .map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        response.setList(libraryDtoList);
        response.setTotalItems(repository.countByIsPrivateAndNameLikeOrGivenAccessListContainsAndNameLikeOrUserAndNameLike(false, libraryNamePattern, user, libraryNamePattern, user, libraryNamePattern));
        return response;
    }

    @Transactional
    @Override
    public LibraryDto addLibraryToFavorites(Long libraryId, Long userId) {
        Library library = findById(libraryId);
        User user = userService.findById(userId);

        library.getUsersFavorite().add(user);
        user.getFavoriteLibraries().add(library);
        repository.save(library);

        return convertToDto(library, userId);
    }

    @Transactional
    @Override
    public LibraryDto removeLibraryFromFavorites(Long libraryId, Long userId) {
        Library library = findById(libraryId);
        User user = userService.findById(userId);

        library.getUsersFavorite().remove(user);
        user.getFavoriteLibraries().remove(library);
        repository.save(library);

        return convertToDto(library, userId);
    }

    @Override
    public LibraryResponse getUserFavoriteLibrariesPaginated(Long userId, String libraryName, Pageable pageable) {
        String libraryNamePattern = libraryName + "%";

        List<LibraryDto> libraryDtoList = repository.findLibrariesByUsersFavorite_UserIdAndNameLike(userId, libraryNamePattern, pageable).stream()
                .map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        LibraryResponse libraryResponse = new LibraryResponse();
        libraryResponse.setTotalItems(repository.countByUsersFavorite_UserIdAndNameLike(userId, libraryNamePattern));
        libraryResponse.setList(libraryDtoList);

        return libraryResponse;
    }

    @Transactional
    @Override
    public LibraryDto addAccessToLibraryByUserEmail(AddLibraryAccessDto addLibraryAccessDto) {
        Library library = findById(addLibraryAccessDto.getLibraryId());

        List<User> users = addLibraryAccessDto.getEmailList().stream()
                .map(userService::findByEmail)
                .collect(Collectors.toList());

        library.getGivenAccessList().addAll(users);
        users.forEach(u -> {
            u.getGivenAccessLibraries().add(library);
            mailService.sendAddLibraryAccessMail(u, library);
        });

        return convertToDto(repository.save(library));
    }

    @Transactional
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
            mailService.sendRemoveLibraryAccessMail(u, library);
        });

        return convertToDto(repository.save(library));
    }

    @Transactional
    @Override
    public LibraryDto addAccessToLibraryByToken(AddLibraryAccessByTokenDto addLibraryAccessDto) {
        Library library = repository.findByAccessToken(addLibraryAccessDto.getAccessToken())
                .orElseThrow(() -> new LibraryNotFoundException("Cannot find library by token=" + addLibraryAccessDto.getAccessToken()));

        User user = userService.findById(addLibraryAccessDto.getUserId());

        library.getGivenAccessList().add(user);
        user.getGivenAccessLibraries().add(library);

        mailService.sendAddLibraryAccessMail(user, library);

        return convertToDto(repository.save(library));
    }

    @Override
    public LibraryResponse getLibraryGivenAccessListByUser(Long userId, String libraryName, Pageable pageable) {
        String libraryNamePattern = libraryName + "%";

        List<LibraryDto> libraryDtoList = repository.findLibrariesByGivenAccessList_UserIdAndNameLike(userId, libraryNamePattern, pageable)
                .stream().map(l -> convertToDto(l, userId))
                .collect(Collectors.toList());

        LibraryResponse libraryResponse = new LibraryResponse();
        libraryResponse.setList(libraryDtoList);
        libraryResponse.setTotalItems(repository.countByGivenAccessList_UserIdAndNameLike(userId, libraryNamePattern));

        return libraryResponse;
    }

    @Transactional
    @Override
    public LibraryDto addEventToLibrary(AddEventToLibraryDto addEventToStreamDto) {
        Library library = findById(addEventToStreamDto.getLibraryId());
        List<Event> eventList = addEventToStreamDto.getEventIdsList().stream()
                .map(eventService::findById)
                .peek(event -> event.setLibrary(library))
                .collect(Collectors.toList());

        library.getEvents().addAll(eventList);

        return convertToDto(library);
    }

    @Transactional
    @Override
    public LibraryDto deleteEventFromLibrary(Long libraryId, Long eventId) {
        Library library = findById(libraryId);
        Event event = eventService.findById(eventId);

        library.getEvents().remove(event);
        event.setLibrary(null);
        event.setStream(null);

        return convertToDto(library);
    }

    @Override
    public Library findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id=" + id));
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
