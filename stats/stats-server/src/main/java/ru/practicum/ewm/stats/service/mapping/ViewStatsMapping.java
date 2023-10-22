package ru.practicum.ewm.stats.service.mapping;

import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.domain.ViewStats;

public class ViewStatsMapping {
    public static ViewStatsDto toViewStatsDto(ViewStats hits) {
        return new ViewStatsDto(hits.getApp(), hits.getUri(), hits.getHits());
    }
}
