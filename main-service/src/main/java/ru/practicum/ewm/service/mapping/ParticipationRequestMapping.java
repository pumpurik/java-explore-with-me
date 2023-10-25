package ru.practicum.ewm.service.mapping;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.domain.ParticipationRequest;
import ru.practicum.ewm.dto.ParticipationRequestDto;

@Mapper(componentModel = "spring", uses = {EventMapping.class, UserMapping.class})
public interface ParticipationRequestMapping {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto participationRequestToDto(ParticipationRequest participationRequest);

    @InheritInverseConfiguration
    ParticipationRequest dtoToParticipation(ParticipationRequestDto participationRequestDto);

}
