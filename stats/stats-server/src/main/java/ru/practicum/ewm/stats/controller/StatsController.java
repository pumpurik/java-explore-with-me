package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    @PostMapping(path = "/hit")
    public ResponseEntity<Void> createHit(@RequestBody EndpointHitDto endpointHitDto) {
        statsService.createHit(endpointHitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam("start") @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                                                       @RequestParam("end") @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                                                       @RequestParam(value = "uris", required = false) String[] uris,
                                                       @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique
    ) {
        return new ResponseEntity<>(statsService.getStats(start, end, uris == null ? Collections.emptyList() : Arrays.asList(uris), unique), HttpStatus.OK);
    }
}
