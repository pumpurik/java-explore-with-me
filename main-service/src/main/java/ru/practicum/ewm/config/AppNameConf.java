package ru.practicum.ewm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppNameConf {
    @Value("${spring.application.name}")
    private String appName;
}
