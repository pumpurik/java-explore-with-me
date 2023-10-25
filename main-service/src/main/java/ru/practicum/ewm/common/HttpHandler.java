package ru.practicum.ewm.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.stats.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HttpHandler implements HandlerInterceptor {
    private final StatsClient statsClient;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        statsClient.addStat(EndpointHitDto.builder().app(appName).ip(request.getLocalAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now()).build());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
