package ru.practicum.ewm.service.mapping;

import org.mapstruct.*;
import ru.practicum.ewm.domain.Category;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.dto.event.*;

@Mapper(componentModel = "spring", uses = {CategoryMapping.class, UserMapping.class, ParticipationRequestMapping.class})
public interface EventMapping {
    EventFullDto eventToEventFullDto(Event event);

//    @Mapping()
//    EventRequestStatusUpdateRequest eventToEventRequestStatusUpdateRequest(Event event);
//
//    EventRequestStatusUpdateResult eventToEventRequestStatusUpdateResult(Event event);

    EventShortDto eventToEventShortDto(Event event);

    NewEventDto eventToNewEventDto(Event event);

    UpdateEventAdminRequest eventToUpdateEventAdminRequest(Event event);

    UpdateEventUserRequest eventToUpdateEventUserRequest(Event event);

//    Long mapCategoryToLong(Category event);

    //    @AfterMapping
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
