package ua.knu.knudev.knuhubcommon.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Component
public class HelperService {

    public static  <T> T getOrDefault(T newValue, T currentValue) {
        return newValue != null ? newValue : currentValue;
    }

    public static <T, R> R getOrDefault(T newValue, R currentValue, Function<T, R> mapper) {
        return newValue != null ? Objects.requireNonNullElse(mapper.apply(newValue), currentValue) : currentValue;
    }

    public static <T, ID> List<T> extractEntitiesFromIds(Collection<ID> ids, JpaRepository<T, ID> repository) {
        return ids != null ? repository.findAllById(ids) : Collections.emptyList();
    }

    public static <T, ID, E extends RuntimeException> T extractEntityById(ID id, JpaRepository<T, ID> repository,
                                                                          Function<String, E> exceptionSupplier) {
        return repository.findById(id).orElseThrow(
                () -> exceptionSupplier.apply("Entity with id " + id + " not found"));
    }
}
