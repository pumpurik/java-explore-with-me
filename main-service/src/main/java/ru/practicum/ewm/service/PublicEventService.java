package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.enums.SortEnum;
import ru.practicum.ewm.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            boolean onlyAvailable,
            SortEnum sort,
            Integer from,
            Integer size
    ) throws NotFoundException;

    EventFullDto getEvent(Long id) throws NotFoundException;
}
