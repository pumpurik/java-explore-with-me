package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/ratings/events")
public class RatingEventController {
    @GetMapping
    ResponseEntity<List<EventShortDto>> getEventsRating() {
        return null;
    }

    @GetMapping("/{id}")
    ResponseEntity<EventFullDto> getEventRating() {
        return null;
    }

}
