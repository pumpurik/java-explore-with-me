package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.enums.EventStateEnum;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getEvents(
            List<Long> users,
            List<EventStateEnum> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Integer from,
            Integer size
    );

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) throws NotFoundException, ConflictException;
}
