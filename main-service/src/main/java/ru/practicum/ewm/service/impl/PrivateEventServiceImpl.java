package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.domain.Category;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.domain.User;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.enums.EventStateEnum;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.PrivateEventService;
import ru.practicum.ewm.service.mapping.EventMapping;
import ru.practicum.ewm.service.mapping.LocationMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapping eventMapping;
    private final LocationMapping locationMapping;
    private final CategoryRepository categoryRepository;


    @Override
    public List<EventShortDto> getEventsUser(Long userId, Integer from, Integer size) throws NotFoundException {
        if (from != null && size != null) {
            return eventRepository.findByInitiator(userRepository.findById(userId).orElseThrow(() -> {
                return new NotFoundException();
            }), PageRequest.of(from, size)).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList());
        } else {
            return eventRepository.findByInitiator(userRepository.findById(userId).orElseThrow(() -> {
                return new NotFoundException();
            })).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList());
        }
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        Category category = null;
        if (newEventDto != null) {
            category = categoryRepository.findById(newEventDto.getCategory()).orElse(null);
        }
        Event event = eventMapping.newCategoryDtoToCategory(newEventDto, category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        return eventMapping.eventToEventFullDto(eventRepository.save(event));
    }


    @Override
    public EventFullDto getEventUser(Long userId, Long eventId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        return eventMapping.eventToEventFullDto(eventRepository.findByInitiatorAndId(user, eventId).orElseThrow(() -> {
            return new NotFoundException();
        }));
    }

    @Override
    @Transactional
    public EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        Optional<Event> byInitiatorAndId = eventRepository.findByInitiatorAndId(user, eventId);
        if (byInitiatorAndId.isPresent()) {
            Category category = null;
            if (updateEventUserRequest != null) {
                category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElse(null);
                return eventMapping.eventToEventFullDto(mapUpdateEventUserRequestToEvent(updateEventUserRequest, byInitiatorAndId.get(), category));
            }

        }
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setState(EventStateEnum.CANCELED);
        return eventFullDto;
    }

    private Event mapUpdateEventUserRequestToEvent(UpdateEventUserRequest update, Event event, Category category) {
        if (update.getAnnotation() != null) event.setAnnotation(update.getAnnotation());
        if (update.getEventDate() != null) event.setEventDate(update.getEventDate());
        if (update.getCategory() != null) event.setCategory(category);
        if (update.getDescription() != null) event.setDescription(update.getDescription());
        if (update.getTitle() != null) event.setTitle(update.getTitle());
        if (update.getLocationDto() != null)
            event.setLocation(locationMapping.locationDtoToLocation(update.getLocationDto()));
        if (update.getStateAction() != null) event.setState(EventStateEnum.valueOf(update.getStateAction().name()));
        event.setParticipantLimit(update.getParticipantLimit());
        event.setRequestModeration(update.isRequestModeration());
        event.setPaid(update.isPaid());
        return event;
    }
}
