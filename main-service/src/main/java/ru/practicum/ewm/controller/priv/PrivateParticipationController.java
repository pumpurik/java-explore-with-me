package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class PrivateParticipationController {
    private final ParticipationRequestService participationRequestService;
    private final StatsClient statsClient;

    @GetMapping("/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestParticipation(@PathVariable Long userId) {
        return new ResponseEntity<>(participationRequestService.getRequestParticipation(userId), HttpStatus.OK);
    }

    @PostMapping("/requests")
    public ResponseEntity<ParticipationRequestDto> createRequestParticipation(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) throws NotFoundException, ConflictException {
        return new ResponseEntity<>(participationRequestService.createRequestParticipation(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequestParticipation(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) throws NotFoundException {
        return new ResponseEntity<>(participationRequestService.cancelRequestParticipation(userId, requestId), HttpStatus.OK);
    }

    @GetMapping("/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventUserRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return new ResponseEntity<>(participationRequestService.getEventUserRequest(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventUserRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody(required = false) EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) throws NotFoundException, ConflictException {
        return new ResponseEntity<>(participationRequestService
                .updateEventUserRequest(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }
}
