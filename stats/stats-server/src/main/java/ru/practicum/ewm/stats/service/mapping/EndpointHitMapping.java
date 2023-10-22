package ru.practicum.ewm.stats.service.mapping;

import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.stats.domain.EndpointHit;

public class EndpointHitMapping {
    public static EndpointHit toEntity(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }

}
