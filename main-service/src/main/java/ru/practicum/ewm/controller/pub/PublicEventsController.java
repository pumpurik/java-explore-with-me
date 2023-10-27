package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.config.AppNameConf;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.enums.SortEnum;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.service.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.practicum.ewm.common.Const.PATTERN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
@Validated
public class PublicEventsController {
    private final PublicEventService publicEventService;
    private final StatsClient statsClient;
    private final AppNameConf appNameConf;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(
            @RequestParam(required = false) @Size(min = 2) String text,
            @RequestParam(required = false) Long[] categories,
            @RequestParam(required = false) boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = PATTERN) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "true") boolean onlyAvailable,
            @RequestParam(required = false) SortEnum sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) throws NotFoundException {
        return new ResponseEntity<>(publicEventService.getEvents(text,
                categories == null ? null : Arrays.asList(categories), paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id, HttpServletRequest request) throws NotFoundException {
        ResponseEntity<Integer> starts = statsClient.getStarts("/stats/" + appNameConf.getAppName(), request.getRequestURI(), true);
        EventFullDto event = publicEventService.getEvent(id);
        event.setViews(starts.getBody());
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
