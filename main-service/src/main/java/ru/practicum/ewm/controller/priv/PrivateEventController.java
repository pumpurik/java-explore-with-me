package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.PrivateEventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class PrivateEventController {
    private final PrivateEventService eventService;
    private final StatsClient statsClient;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getEventsUser(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size
    ) throws NotFoundException {
        return new ResponseEntity<>(eventService.getEventsUser(userId, from, size), HttpStatus.OK);
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> addNewEvent(
            @PathVariable Long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) throws NotFoundException {
        return new ResponseEntity<>(eventService.addNewEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventUser(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) throws NotFoundException {

        return new ResponseEntity<>(eventService.getEventUser(userId, eventId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventUser(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody(required = false) @Valid UpdateEventUserRequest updateEventUserRequest
    ) throws NotFoundException {
        return new ResponseEntity<>(eventService.updateEventUser(userId, eventId, updateEventUserRequest), HttpStatus.OK);
    }

}
