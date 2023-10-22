package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStatsDto;
import ru.practicum.ewm.stats.domain.ViewStats;
import ru.practicum.ewm.stats.repository.EndpointHitRepository;
import ru.practicum.ewm.stats.service.mapping.EndpointHitMapping;
import ru.practicum.ewm.stats.service.mapping.ViewStatsMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public void createHit(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(EndpointHitMapping.toEntity(endpointHitDto));
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> statsUnique = Collections.emptyList();
        if (unique) {
            statsUnique = endpointHitRepository.findStatsUnique(start, end, uris);
        } else {
            statsUnique = endpointHitRepository.findStatsNotUnique(start, end, uris);
        }
        return statsUnique.stream().map(ViewStatsMapping::toViewStatsDto).collect(Collectors.toList());
    }
}
