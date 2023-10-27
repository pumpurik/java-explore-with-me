package ru.practicum.ewm.client.stats;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.stats.EndpointHitDto;

import java.util.List;
import java.util.Map;

public class StatsClient {
    private final RestTemplate rest;

    public StatsClient(String serverUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> getStarts(String path, String start, String end, List<String> uris, boolean unique) {
        HttpEntity<?> requestEntity = new HttpEntity<>(null, defaultHeaders());
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return rest.exchange(path, HttpMethod.GET, requestEntity, Object.class, parameters);
    }

    public ResponseEntity<Integer> getStarts(String path, String url, boolean unique) {
        HttpEntity<?> requestEntity = new HttpEntity<>(null, defaultHeaders());
        Map<String, Object> parameters = Map.of(
                "url", url,
                "unique", unique
        );
        return rest.exchange(path + "?url={url}&unique={unique}", HttpMethod.GET, requestEntity, Integer.class, parameters);
    }

    public ResponseEntity<Object> addStat(String path, EndpointHitDto endpointHitDto) {
        HttpEntity<?> requestEntity = new HttpEntity<>(endpointHitDto, defaultHeaders());
        try {
            return rest.exchange(path, HttpMethod.POST, requestEntity, Object.class);
        } catch (Exception e) {
            return null;
        }
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

}