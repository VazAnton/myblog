package ru.yandex.practicum.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exceptions.BadRequestException;
import ru.yandex.practicum.exceptions.EntityNotFoundException;

import java.util.Map;

@RestControllerAdvice(basePackages = "ru.yandex.practicum")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> onEntityNotFoundException(EntityNotFoundException e) {
        return Map.of("Сущность не найдена.", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> onBadRequestException(BadRequestException e) {
        return Map.of("Ошибка в запросе.", e.getMessage());
    }
}
