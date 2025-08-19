package ua.knu.knudev.knuhubcommon.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
public class HelperService {

    public static <T> T getOrDefault(
            T newValue,
            T currentValue
    ) {
        return newValue != null
                ? newValue
                : currentValue;
    }

    public static <T, R> R getOrDefault(
            T newValue,
            R currentValue,
            Function<T, R> mapper
    ) {
        return newValue != null
                ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue)
                : currentValue;
    }

    public static <T, ID> List<T> extractEntities(
            Collection<ID> ids,
            JpaRepository<T, ID> repository
    ) {
        return ids != null
                ? repository.findAllById(ids)
                : Collections.emptyList();
    }

    public static <T, ID, E extends RuntimeException> T extractEntity(
            ID id,
            JpaRepository<T, ID> repository,
            Function<String, E> exceptionSupplier
    ) {
        return repository.findById(id).orElseThrow(
                () -> exceptionSupplier.apply("Entity with id " + id + " not found"));
    }

    public static <E extends RuntimeException> void validateMultiLanguageFields(
            String en,
            String uk,
            Function<String, E> exceptionSupplier
    ) {
        if (en != null && !en.matches("^[a-zA-Z0-9\\s.,;:'\"\\-()]+$")) {
            throw exceptionSupplier.apply("English field must contain only English letters!");
        }

        if (uk != null && !uk.matches("^[а-яА-ЯіІїЇєЄґҐ0-9\\s.,;:'\"\\-()]+$")) {
            throw exceptionSupplier.apply("Ukrainian field must contain only Ukrainian letters!");
        }
    }

}
