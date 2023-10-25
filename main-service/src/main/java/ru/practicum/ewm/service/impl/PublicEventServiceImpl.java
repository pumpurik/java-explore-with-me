package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.enums.SortEnum;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.PublicEventService;
import ru.practicum.ewm.service.mapping.EventMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapping eventMapping;


    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, boolean onlyAvailable, SortEnum sort, Integer from, Integer size) {
        List<EventShortDto> collect = eventRepository.findAllByPublic(text, categories, paid, rangeStart, rangeEnd)
                .stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList());
        return collect;
    }

    @Override
    public EventShortDto getEvent(Long id) throws NotFoundException {
        return eventMapping.eventToEventShortDto(eventRepository.findById(id).orElseThrow(() -> {
            return new NotFoundException();
        }));
    }
}
