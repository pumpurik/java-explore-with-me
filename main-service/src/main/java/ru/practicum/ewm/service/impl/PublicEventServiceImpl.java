package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.enums.EventStateEnum;
import ru.practicum.ewm.enums.SortEnum;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.PublicEventService;
import ru.practicum.ewm.service.mapping.EventMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapping eventMapping;

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, boolean onlyAvailable, SortEnum sort, Integer from, Integer size) throws NotFoundException {
        if (rangeStart == null) rangeEnd = LocalDateTime.now();
        List<EventShortDto> eventShortDtos;
        if (sort != null) {
            eventShortDtos = onlyAvailable ? eventRepository
                    .findAllByPublicOnlyAvailable(
                            text, categories, paid, rangeStart, rangeEnd, Collections.emptyList(), PageRequest.of(from, size,
                                    Sort.by(Sort.Order.desc(convertSortProperty(sort))))
                    ).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList()) :
                    eventRepository.findAllByPublic(
                            text, categories, paid, rangeStart, rangeEnd, Collections.emptyList(), PageRequest.of(from, size,
                                    Sort.by(Sort.Order.desc(convertSortProperty(sort))))
                    ).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList())
            ;

        } else {
            eventShortDtos = onlyAvailable ? eventRepository
                    .findAllByPublicOnlyAvailable(
                            text, categories, paid, rangeStart, rangeEnd, Collections.emptyList(), PageRequest.of(from, size)
                    ).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList()) :
                    eventRepository.findAllByPublic(
                            text, categories, paid, rangeStart, rangeEnd, Collections.emptyList(), PageRequest.of(from, size)
                    ).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList());

        }
        if (eventShortDtos.isEmpty()) throw new NotFoundException("По заданным фильтрам не найдено ни одного события!");
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEvent(Long id) throws NotFoundException {
        return eventMapping.eventToEventFullDto(eventRepository.findByIdAndState(id, EventStateEnum.PUBLISHED).orElseThrow(() -> {
            log.info("Событие с айди {} не найдено!", id);
            return new NotFoundException(String.format("Событие не найдено c айди %s не найдено!", id));
        }));
    }

    private String convertSortProperty(SortEnum sortEnum) {
        if (sortEnum.equals(SortEnum.VIEWS)) return "views";
        if (sortEnum.equals(SortEnum.RATING)) return "rating";
        else return "eventDate";
    }
}
