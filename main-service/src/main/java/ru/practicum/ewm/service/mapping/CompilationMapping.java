package ru.practicum.ewm.service.mapping;

import org.mapstruct.*;
import ru.practicum.ewm.domain.Compilation;
import ru.practicum.ewm.domain.Event;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = EventMapping.class)
public interface CompilationMapping {
    CompilationDto compilationToDto(Compilation compilation);

    @Mapping(target = "events", source = "events")
    NewCompilationDto compilationToNewCompilationDto(Compilation compilation);

    @Mapping(target = "events", source = "events")
    UpdateCompilationRequest compilationToUpdateCompilationRequest(Compilation compilation);

    @InheritInverseConfiguration
    @Mapping(target = "events", source = "events", qualifiedByName = "mapEventLongToEvent")
    Compilation updateCompilationRequestToCompilation(NewCompilationDto newCategoryDto, @Context List<Event> events);

    @Named("mapEventLongToEvent")
    default List<Event> mapEventLongToEvent(List<Long> eventsLong, @Context List<Event> events) {
        return events;
    }

    List<Long> mapEventToLongList(List<Event> events);

    default Long convertEventToLong(Event event) {
        if (event != null) {
            return event.getId();
        }
        return null;
    }

    @AfterMapping
    default List<Long> convertEventListToLongList(List<Event> events) {
        return events.stream()
                .map(this::convertEventToLong)
                .collect(Collectors.toList());
    }

}
