package ru.practicum.ewm.client.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate rest;

    public StatsClient(String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    ResponseEntity<Object> getStarts(String path, String start, String end, List<String> uris, boolean unique) {
        HttpEntity<?> requestEntity = new HttpEntity<>(null, defaultHeaders());
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return rest.exchange(path, HttpMethod.GET, requestEntity, Object.class, parameters);
    }


    ResponseEntity<Object> addStat(String path, String app, String uri, String ip, LocalDateTime timestamp) {
        HttpEntity<?> requestEntity = new HttpEntity<>(new EndpointHitDto(app, uri, ip, timestamp), defaultHeaders());
        return rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}