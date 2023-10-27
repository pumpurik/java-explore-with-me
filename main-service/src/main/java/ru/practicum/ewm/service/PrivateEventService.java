package ru.practicum.ewm.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getEventsUser(Long userId, PageRequest pageRequest) throws NotFoundException;

    EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws NotFoundException, ConflictException;

    EventFullDto getEventUser(Long userId, Long eventId) throws NotFoundException;

    EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) throws NotFoundException, ConflictException;
}
