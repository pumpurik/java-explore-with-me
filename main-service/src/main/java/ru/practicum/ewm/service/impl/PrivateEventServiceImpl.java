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
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.PrivateEventService;
import ru.practicum.ewm.service.mapping.EventMapping;
import ru.practicum.ewm.service.mapping.LocationMapping;

import java.nio.channels.NotYetBoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.urils.ConvertUtils.convertToState;

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
    public List<EventShortDto> getEventsUser(Long userId, PageRequest pageRequest) throws NotFoundException {
        return eventRepository.findByInitiator(userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        }), pageRequest).stream().map(eventMapping::eventToEventShortDto).collect(Collectors.toList());

    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) throws NotFoundException, ConflictException {
        dateValid(newEventDto.getEventDate());
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> {
            return new NotYetBoundException();
        });
        Event event = eventMapping.newCategoryDtoToCategory(newEventDto, category);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventStateEnum.PENDING);
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
    public EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) throws NotFoundException, ConflictException {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            return new NotFoundException();
        });
        Optional<Event> byInitiatorAndId = eventRepository.findByInitiatorAndId(user, eventId);
        if (byInitiatorAndId.isPresent()) {
            dateValid(byInitiatorAndId.get().getEventDate());
            if (byInitiatorAndId.get().getState().equals(EventStateEnum.PUBLISHED)) throw new ConflictException();
            Category category = null;
            if (updateEventUserRequest != null) {
                if (updateEventUserRequest.getCategory() != null) {
                    category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElse(null);
                }
                Event event = mapUpdateEventUserRequestToEvent(updateEventUserRequest, byInitiatorAndId.get(), category);
                eventRepository.delete(event);
                return eventMapping.eventToEventFullDto(event);
            }

        }
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setState(EventStateEnum.CANCELED);
        return eventFullDto;
    }

    private void dateValid(LocalDateTime eventDate) throws ConflictException {
        if (!eventDate.isAfter(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException();
        }
    }

    private Event mapUpdateEventUserRequestToEvent(UpdateEventUserRequest update, Event event, Category category) {
        if (update.getAnnotation() != null) event.setAnnotation(update.getAnnotation());
        if (update.getEventDate() != null) event.setEventDate(update.getEventDate());
        if (update.getCategory() != null) event.setCategory(category);
        if (update.getDescription() != null) event.setDescription(update.getDescription());
        if (update.getTitle() != null) event.setTitle(update.getTitle());
        if (update.getLocation() != null)
            event.setLocation(locationMapping.locationDtoToLocation(update.getLocation()));
        if (update.getStateAction() != null) event.setState(convertToState(update.getStateAction()));
        if (update.getParticipantLimit() != null) event.setParticipantLimit(update.getParticipantLimit());
        if (update.getRequestModeration() != null) event.setRequestModeration(update.getRequestModeration());
        if (update.getPaid() != null) event.setPaid(update.getPaid());
        return event;
    }
}
