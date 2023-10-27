package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.domain.Category;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.enums.EventStateEnum;
import ru.practicum.ewm.enums.StateActionEnum;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.AdminEventService;
import ru.practicum.ewm.service.mapping.EventMapping;
import ru.practicum.ewm.service.mapping.LocationMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.urils.ConvertUtils.convertToState;
import static ru.practicum.ewm.urils.NullUtils.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final EventMapping eventMapping;
    private final LocationMapping locationMapping;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEvents(List<Long> users, List<EventStateEnum> states, List<Long> categories, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Integer from, Integer size) {
        List<EventFullDto> eventFullDtos =
                eventRepository
                        .findAllByAdmin(users, states, categories,
                                rangeStart, rangeEnd, PageRequest.of(from, size)).stream()
                        .map(eventMapping::eventToEventFullDto).collect(Collectors.toList());
//                eventRepository.findAllByAdmin(users, states, categories, rangeStart, rangeEnd).stream()
//                        .map(eventMapping::eventToEventFullDto).collect(Collectors.toList());
        return eventFullDtos;
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) throws NotFoundException, ConflictException {
        Category category = null;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            return new NotFoundException();
        });
        if (!event.getState().equals(EventStateEnum.PENDING) && equalsIgnoreNullFalse.apply(getOrDefault(() ->
                updateEventAdminRequest.getStateAction().name(), StateActionEnum.PUBLISH_EVENT.name()), StateActionEnum.PUBLISH_EVENT.name())) {
            throw new ConflictException();
        }
        if (event.getState().equals(EventStateEnum.CANCELED) && equalsIgnoreNullFalse.apply(getOrDefault(() ->
                updateEventAdminRequest.getStateAction().name(), StateActionEnum.PUBLISH_EVENT.name()), StateActionEnum.PUBLISH_EVENT.name())) {
            throw new ConflictException();
        }
        if (equalsIgnoreNullFalse.apply(getOrNull(() -> updateEventAdminRequest.getStateAction().name()), StateActionEnum.REJECT_EVENT.name()) &&
                event.getState().equals(EventStateEnum.PUBLISHED)) {
            throw new ConflictException();
        }
        if (updateEventAdminRequest != null) {
            if (updateEventAdminRequest.getCategory() != null) {
                category = categoryRepository.findById(updateEventAdminRequest.getCategory()).orElse(null);
            }
            return eventMapping.eventToEventFullDto(eventRepository.save(mapUpdateEventAdminRequestToEvent(updateEventAdminRequest, event, category)));
        }
        return eventMapping.eventToEventFullDto(eventRepository.save(event));

    }

    private Event mapUpdateEventAdminRequestToEvent(UpdateEventAdminRequest update, Event event, Category category) {
        if (update.getAnnotation() != null) event.setAnnotation(update.getAnnotation());
        if (update.getEventDate() != null) event.setEventDate(update.getEventDate());
        if (category != null) event.setCategory(category);
        if (update.getDescription() != null) event.setDescription(update.getDescription());
        if (update.getTitle() != null) event.setTitle(update.getTitle());
        if (update.getLocationDto() != null)
            event.setLocation(locationMapping.locationDtoToLocation(update.getLocationDto()));
        if (update.getStateAction() != null) event.setState(convertToState(update.getStateAction()));
        if (update.getParticipantLimit() != null) event.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() != null) event.setRequestModeration(update.getRequestModeration());
        if (update.getPaid() != null) event.setPaid(update.getPaid());
        return event;
    }


}
