package ru.practicum.ewm.service.mapping;

import org.mapstruct.*;
import ru.practicum.ewm.domain.Category;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.urils.NullUtils;

@Mapper(componentModel = "spring", uses = {CategoryMapping.class, UserMapping.class, ParticipationRequestMapping.class},
        imports = NullUtils.class)
public interface EventMapping {
    @Mapping(target = "confirmedRequests", expression = "java(NullUtils.getOrDefault(()->event.getConfirmedRequests().size(), 0))")
    EventFullDto eventToEventFullDto(Event event);

    @Mapping(target = "confirmedRequests", expression = "java(NullUtils.getOrDefault(()->event.getConfirmedRequests().size(), 0))")
    EventShortDto eventToEventShortDto(Event event);

    NewEventDto eventToNewEventDto(Event event);

    UpdateEventAdminRequest eventToUpdateEventAdminRequest(Event event);

    UpdateEventUserRequest eventToUpdateEventUserRequest(Event event);

    default Long convertEventToLong(Category category) {
        if (category != null) {
            return category.getId();
        }
        return null;
    }

    @InheritInverseConfiguration
    @Mapping(target = "category", source = "category", qualifiedByName = "mapLongToCategory")
    Event newCategoryDtoToCategory(NewEventDto newEventDto, @Context Category category);

    @Named("mapLongToCategory")
    default Category mapLongToCategory(Long categoryId, @Context Category category) {
        return category;
    }


}
