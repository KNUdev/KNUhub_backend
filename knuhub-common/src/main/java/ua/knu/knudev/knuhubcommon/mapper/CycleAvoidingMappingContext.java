package ua.knu.knudev.knuhubcommon.mapper;

import org.springframework.stereotype.Component;
import java.util.IdentityHashMap;
import java.util.Map;

@Component
public class CycleAvoidingMappingContext {
    private Map<Object, Object> knownInstances = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getMappedInstance(Object source, Class<T> targetType) {
        return (T) knownInstances.get(source);
    }

    public void storeMappedInstance(Object source, Object target) {
        knownInstances.put(source, target);
    }

    public boolean isKnownInstance(Object source) {
        return knownInstances.containsKey(source);
    }
}