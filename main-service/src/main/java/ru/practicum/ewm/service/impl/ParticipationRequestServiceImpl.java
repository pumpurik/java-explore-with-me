package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.ParticipationRequest;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.enums.ParticipationRequestStatusEnum;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.ParticipationRequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.ParticipationRequestService;
import ru.practicum.ewm.service.mapping.ParticipationRequestMapping;

import java.time.LocalDateTime;
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
    public ParticipationRequestDto createRequestParticipation(Long userId, Long eventId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            return new NotFoundException();
        });

        return participationRequestMapping.participationRequestToDto(participationRequestRepository.save(ParticipationRequest.builder().created(LocalDateTime.now())
                .requester(user).status(ParticipationRequestStatusEnum.PENDING).event(event).build()));
    }

    @Override
    public ParticipationRequestDto cancelRequestParticipation(Long userId, Long requestId) throws NotFoundException {
        ParticipationRequest byId = participationRequestRepository.findById(requestId).orElseThrow(() -> {
            return new NotFoundException();
        });
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        byId.getRequester();

        return null;
    }

    @Override
    public List<ParticipationRequestDto> getEventUserRequest(Long userId, Long eventId) {
        return participationRequestRepository.findAllByEventIdAndRequesterId(eventId, userId).stream()
                .map(participationRequestMapping::participationRequestToDto).collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto updateEventUserRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<ParticipationRequest> allByEventIdAndRequesterId = participationRequestRepository.findAllByEventIdAndRequesterId(eventId, userId);
        return null;
    }
}
