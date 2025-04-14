package ru.yandex.practicum.cofigurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
@Import({PersistenceConfig.class,
        WebConfig.class,
        ThymeleafConfiguration.class})
public class AppConfig {

    @Bean(value = "resourceLoader")
    public ResourceLoader createResourceLoader() {
        return new DefaultResourceLoader();
    }
}