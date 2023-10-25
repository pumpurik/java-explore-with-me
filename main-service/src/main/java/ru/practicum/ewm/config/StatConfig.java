package ru.practicum.ewm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.client.stats.StatsClient;

@Configuration
public class StatConfig {
    @Bean
    public StatsClient statsClient(@Value("stats.server.url") String statUrl, RestTemplateBuilder builder) {
        return new StatsClient(statUrl, builder);
    }
}
