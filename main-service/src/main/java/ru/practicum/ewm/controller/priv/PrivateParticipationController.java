package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateParticipationController {
    private final ParticipationRequestService participationRequestService;
    private final StatsClient statsClient;

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestParticipation(@PathVariable Long userId) {
        return new ResponseEntity<>(participationRequestService.getRequestParticipation(userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> createRequestParticipation(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) throws NotFoundException {
        return new ResponseEntity<>(participationRequestService.createRequestParticipation(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequestParticipation(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) throws NotFoundException {
        return new ResponseEntity<>(participationRequestService.cancelRequestParticipation(userId, requestId), HttpStatus.OK);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventUserRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        return new ResponseEntity<>(participationRequestService.getEventUserRequest(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<ParticipationRequestDto> updateEventUserRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        return new ResponseEntity<>(participationRequestService
                .updateEventUserRequest(userId, eventId, eventRequestStatusUpdateRequest), HttpStatus.OK);
    }
}
