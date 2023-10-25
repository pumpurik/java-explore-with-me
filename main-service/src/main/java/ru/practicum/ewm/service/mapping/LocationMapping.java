package ru.practicum.ewm.service.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import ru.practicum.ewm.domain.Location;
import ru.practicum.ewm.dto.LocationDto;

@Mapper(componentModel = "spring")
public interface LocationMapping {
    LocationDto locationToLocationDto(Location location);

    @InheritInverseConfiguration
    Location locationDtoToLocation(LocationDto locationDto);
}
