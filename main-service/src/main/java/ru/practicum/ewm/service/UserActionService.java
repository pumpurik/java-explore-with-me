package ru.practicum.ewm.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.rating.UserActionDto;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

public interface UserActionService {
    UserActionDto postLike(Long userId, Long eventId) throws NotFoundException, ConflictException;

    UserActionDto postDislike(Long userId, Long eventId) throws NotFoundException, ConflictException;

    void deleteLikeOrDislike(Long userId, Long eventId) throws NotFoundException, ConflictException;

    UserActionDto getAction(Long userId, Long eventId) throws NotFoundException;

    List<UserActionDto> getActions(PageRequest pageRequest);
}
