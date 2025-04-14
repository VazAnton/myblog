package ru.yandex.practicum.cofigurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"ru.yandex.practicum"})
@PropertySource(value = "classpath:application.properties")
public class WebConfig {
}
