package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getRequestParticipation(Long userId);

    ParticipationRequestDto createRequestParticipation(Long userId, Long eventId) throws NotFoundException;

    ParticipationRequestDto cancelRequestParticipation(Long userId, Long requestId) throws NotFoundException;

    List<ParticipationRequestDto> getEventUserRequest(Long userId, Long eventId);

    ParticipationRequestDto updateEventUserRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
