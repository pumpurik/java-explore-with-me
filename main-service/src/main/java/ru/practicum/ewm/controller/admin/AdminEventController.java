package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.AdminEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.practicum.ewm.common.Const.PATTERN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;
    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getEvents(
            @RequestParam(required = false) Long[] users,
            @RequestParam(required = false) String[] states,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer size

    ) {
        return new ResponseEntity<>(adminEventService.getEvents(users == null ? null : Arrays.asList(users),
                states == null ? null : Arrays.asList(states),
                categories == null ? null : Arrays.asList(categories), rangeStart, rangeEnd, from, size), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long eventId, @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) throws NotFoundException {
        return new ResponseEntity<>(adminEventService.updateEvent(eventId, updateEventAdminRequest), HttpStatus.OK);
    }
}
