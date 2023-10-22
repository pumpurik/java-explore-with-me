package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.domain.EndpointHit;
import ru.practicum.ewm.stats.domain.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    Optional<EndpointHit> findByAppAndUri(String app, String uri);

    @Query("SELECT new ru.practicum.ewm.stats.domain.ViewStats(a.app, a.uri, COUNT(distinct a.ip) as hits) FROM EndpointHit as a " +
            "WHERE a.timestamp BETWEEN :start AND :end AND a.uri IN :uris GROUP BY a.app, a.uri ORDER BY hits DESC")
    List<ViewStats> findStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.domain.ViewStats(a.app, a.uri, COUNT(distinct a.ip) as hits) FROM EndpointHit as a " +
            "WHERE a.timestamp BETWEEN :start AND :end GROUP BY a.app, a.uri ORDER BY hits DESC")
    List<ViewStats> findStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.stats.domain.ViewStats(a.app, a.uri, COUNT(*) as hits) FROM EndpointHit as a " +
            "WHERE a.timestamp BETWEEN :start AND :end AND a.uri IN :uris GROUP BY a.app, a.uri ORDER BY hits DESC")
    List<ViewStats> findStatsNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.domain.ViewStats(a.app, a.uri, COUNT(*) as hits) FROM EndpointHit as a " +
            "WHERE a.timestamp BETWEEN :start AND :end GROUP BY a.app, a.uri ORDER BY hits DESC")
    List<ViewStats> findStatsNotUnique(LocalDateTime start, LocalDateTime end);
}
