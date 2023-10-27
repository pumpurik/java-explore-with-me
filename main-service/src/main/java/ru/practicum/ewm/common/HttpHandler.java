package ru.practicum.ewm.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.config.AppNameConf;
import ru.practicum.ewm.dto.stats.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HttpHandler implements HandlerInterceptor {
    private final StatsClient statsClient;
    private final AppNameConf appNameConf;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        statsClient.addStat("/hit", EndpointHitDto.builder().app(appNameConf.getAppName()).ip(request.getLocalAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now()).build());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
