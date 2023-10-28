package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.ParticipationRequest;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.ewm.enums.EventStateEnum;
import ru.practicum.ewm.enums.ParticipationRequestStatusEnum;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.ParticipationRequestService;
import ru.practicum.ewm.service.mapping.ParticipationRequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestMapping participationRequestMapping;


    @Override
    public List<ParticipationRequestDto> getRequestParticipation(Long userId) {
        return participationRequestRepository.findAllByRequesterId(userId).stream()
                .map(participationRequestMapping::participationRequestToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequestParticipation(Long userId, Long eventId) throws NotFoundException, ConflictException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь c айди {} не найден!", userId);
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Событие c айди {} не найдено!", eventId);
            return new NotFoundException(String.format("Событие c айди %s не найдено!", eventId));
        });
        ParticipationRequestStatusEnum confirmed = ParticipationRequestStatusEnum.CONFIRMED;
        if (event.getInitiator().equals(user)) {
            log.info("Инициатор события {} не может добавить запрос на участие в своём событии!", user);
            throw new ConflictException(String.format("Инициатор события %s не может добавить запрос на участие в своём событии!", user));
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests().stream()
                .filter(s -> s.getStatus().equals(ParticipationRequestStatusEnum.CONFIRMED))
                .count()) {
            log.info("У события {} достигнут лимит запросов на участие!", event);
            throw new ConflictException(String.format("У события %s достигнут лимит запросов на участие!", event));
        }
        if (participationRequestRepository.findByEventAndRequester(event, user).isPresent()) {
            log.info("Нельзя добавить повторный запрос от пользователя {} на событие {}!", event, user);
            throw new ConflictException("Нельзя добавить повторный запрос!");
        }
        if (!event.getState().equals(EventStateEnum.PUBLISHED)) {
            log.info("Событие {} не опубликовано!", event);
            throw new ConflictException(String.format("Нельзя участвовать в неопубликованном событии %s!", event));
        }
        if (event.isRequestModeration()) {
            if (event.getInitiator().equals(user)) {
                log.info("Инициатор события {} не может добавить запрос на участие в своём событии!", user);
                throw new ConflictException(String.format("Инициатор события %s не может добавить запрос на участие в своём событии!", user));
            }
            confirmed = event.getParticipantLimit() == 0 ? ParticipationRequestStatusEnum.CONFIRMED : ParticipationRequestStatusEnum.PENDING;
        }


        return participationRequestMapping.participationRequestToDto(participationRequestRepository.save(ParticipationRequest.builder().created(LocalDateTime.now())
                .requester(user).status(confirmed).event(event).build()));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequestParticipation(Long userId, Long requestId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь c айди {} не найден!", userId);
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        });

        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> {
            log.info("Запрос c айди {} не найден!", requestId);
            return new NotFoundException(String.format("Запрос c айди %s не найден!", requestId));
        });
        participationRequest.setStatus(ParticipationRequestStatusEnum.CANCELED);
        return participationRequestMapping.participationRequestToDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDto> getEventUserRequest(Long userId, Long eventId) {
        return participationRequestRepository.findByEventId(eventId).stream()
                .map(participationRequestMapping::participationRequestToDto).collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateEventUserRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest requestUpdate) throws NotFoundException, ConflictException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.info("Пользователь c айди {} не найден!", userId);
            return new NotFoundException(String.format("Пользователь c айди %s не найден!", userId));
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.info("Событие c айди {} не найдено!", eventId);
            return new NotFoundException(String.format("Событие c айди %s не найдено!", eventId));
        });

        List<ParticipationRequest> byIdIn = requestUpdate == null ?
                participationRequestRepository.findByEventId(eventId) :
                participationRequestRepository.findByIdIn(requestUpdate.getRequestIds());

        ParticipationRequestStatusEnum statusEnum = requestUpdate != null && requestUpdate.getStatus() != null ?
                ParticipationRequestStatusEnum.valueOf(requestUpdate.getStatus().name()) :
                ParticipationRequestStatusEnum.CONFIRMED;

        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        long count = event.getConfirmedRequests().stream()
                .filter(s -> s.getStatus().equals(ParticipationRequestStatusEnum.CONFIRMED))
                .count();

        for (ParticipationRequest request : byIdIn) {
            if (request.getEvent().isRequestModeration() && request.getEvent().getParticipantLimit() != 0) {
                if (count >= request.getEvent().getParticipantLimit()) {
                    log.info("Достигнут лимит {} по заявкам на событие {}!", request.getEvent().getParticipantLimit(), request.getEvent());
                    throw new ConflictException("Достигнут лимит по заявкам на данное событие!");
                }
                if (!request.getStatus().equals(ParticipationRequestStatusEnum.PENDING)) {
                    log.info("Cтатус можно изменить только у заявок, находящихся в состоянии ожидания! {}", request);
                    throw new ConflictException("Cтатус можно изменить только у заявок, находящихся в состоянии ожидания!");
                }
                request.setStatus(statusEnum);
            } else {
                request.setStatus(statusEnum);
            }

            if (statusEnum.equals(ParticipationRequestStatusEnum.REJECTED)) {
                rejectedRequests.add(participationRequestMapping.participationRequestToDto(request));
            } else {
                confirmedRequests.add(participationRequestMapping.participationRequestToDto(request));
            }
            count++;
        }

        if (count > event.getParticipantLimit()) {
            byIdIn.forEach(r -> r.setStatus(ParticipationRequestStatusEnum.REJECTED));
            rejectedRequests = byIdIn.stream()
                    .map(participationRequestMapping::participationRequestToDto)
                    .collect(Collectors.toList());
        }

        participationRequestRepository.saveAll(byIdIn);

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmedRequests);
        eventRequestStatusUpdateResult.setRejectedRequests(rejectedRequests);

        return eventRequestStatusUpdateResult;
    }

}
