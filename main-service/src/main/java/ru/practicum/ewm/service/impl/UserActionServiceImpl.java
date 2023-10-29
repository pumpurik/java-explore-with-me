package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.domain.UserAction;
import ru.practicum.ewm.dto.rating.UserActionDto;
import ru.practicum.ewm.enums.ActionTypeEnum;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserActionRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.UserActionService;
import ru.practicum.ewm.service.mapping.UserActionMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {
    private final UserActionRepository userActionRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserActionMapping userActionMapping;

    @Override
    @Transactional
    public UserActionDto postLike(Long userId, Long eventId) throws NotFoundException, ConflictException {
        UserAction userAction = actionUserValid(userId, eventId, true);
        userAction.setActionType(ActionTypeEnum.LIKE);
        return userActionMapping.toDto(userActionRepository.save(userAction));

    }

    @Override
    @Transactional
    public UserActionDto postDislike(Long userId, Long eventId) throws NotFoundException, ConflictException {
        UserAction userAction = actionUserValid(userId, eventId, false);
        userAction.setActionType(ActionTypeEnum.DISLIKE);
        return userActionMapping.toDto(userActionRepository.save(userAction));
    }

    private UserAction actionUserValid(Long userId, Long eventId, boolean isLike) throws NotFoundException, ConflictException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь c айди {} не найден!", userId);
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Событие c айди {} не найдено!", eventId);
            return new NotFoundException(String.format("Событие c айди %s не найдено!", eventId));
        });
        Map<ActionTypeEnum, Long> actionCounts = event.getUserAction().stream()
                .collect(Collectors.groupingBy(UserAction::getActionType, Collectors.counting()));

        long likes = actionCounts.getOrDefault(ActionTypeEnum.LIKE, 0L);
        long dislikes = actionCounts.getOrDefault(ActionTypeEnum.DISLIKE, 0L);
        int action = isLike ? 1 : -1;
        event.setRating(((likes - dislikes) * (likes + dislikes) + action) / (likes + dislikes + 1));
        eventRepository.save(event);
        Optional<UserAction> byUserAndEvent = userActionRepository.findByUserAndEvent(user, event);
        if (byUserAndEvent.isPresent()) {
            log.info("Пользователь по id = {} уже лайкнул eventId = {}", userId, eventId);
            throw new ConflictException(String.format("Пользователь по id = %s уже лайкнул eventId = %s", userId, eventId));
        }
        UserAction userAction = new UserAction();
        userAction.setEvent(event);
        userAction.setUser(user);
        return userAction;
    }

    @Override
    @Transactional
    public void deleteLikeOrDislike(Long userId, Long eventId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь c айди {} не найден!", userId);
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Событие c айди {} не найдено!", eventId);
            return new NotFoundException(String.format("Событие c айди %s не найдено!", eventId));
        });
        userActionRepository.delete(userActionRepository.findByUserAndEvent(user, event).orElseThrow(() -> {
            log.info("UserAction не найдено!");
            return new NotFoundException("UserAction не найдено!");
        }));
    }

    @Override
    public UserActionDto getAction(Long userId, Long eventId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь c айди {} не найден!", userId);
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Событие c айди {} не найдено!", eventId);
            return new NotFoundException(String.format("Событие c айди %s не найдено!", eventId));
        });
        return userActionMapping.toDto(userActionRepository.findByUserAndEvent(user, event).orElseThrow(() -> {
            log.info("UserAction не найдено!");
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        }));
    }

    @Override
    public List<UserActionDto> getActions(PageRequest pageRequest) {
        return userActionRepository.findAll(pageRequest).stream().map(userActionMapping::toDto).collect(Collectors.toList());
    }
}
