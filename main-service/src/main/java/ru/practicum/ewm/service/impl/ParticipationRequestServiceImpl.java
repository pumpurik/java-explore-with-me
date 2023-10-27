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
            return new NotFoundException();
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            return new NotFoundException();
        });
        ParticipationRequestStatusEnum confirmed = ParticipationRequestStatusEnum.CONFIRMED;
        if (event.getInitiator().equals(user)) {
            throw new ConflictException();
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests().stream()
                .filter(s -> s.getStatus().equals(ParticipationRequestStatusEnum.CONFIRMED))
                .count()) {
            throw new ConflictException();
        }
        if (participationRequestRepository.findByEventAndRequester(event, user).isPresent()) {
            throw new ConflictException();
        }
        if (!event.getState().equals(EventStateEnum.PUBLISHED)) {
            throw new ConflictException();
        }
        if (event.isRequestModeration()) {
            if (event.getInitiator().equals(user)) {
                throw new ConflictException();
            }
            confirmed = event.getParticipantLimit() == 0 ? ParticipationRequestStatusEnum.CONFIRMED : ParticipationRequestStatusEnum.PENDING;
        }


        return participationRequestMapping.participationRequestToDto(participationRequestRepository.save(ParticipationRequest.builder().created(LocalDateTime.now())
                .requester(user).status(confirmed).event(event).build()));
    }

    @Override
    public ParticipationRequestDto cancelRequestParticipation(Long userId, Long requestId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });

        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> {
            return new NotFoundException();
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
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);

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
                    throw new ConflictException();
                }
                if (!request.getStatus().equals(ParticipationRequestStatusEnum.PENDING)) {
                    throw new ConflictException();
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
