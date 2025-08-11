package ua.knu.knudev.peoplemanagement.service;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Component
public class HelperService {

    protected static  <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    protected static <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

}
