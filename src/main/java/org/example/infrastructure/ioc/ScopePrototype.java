package org.example.infrastructure.ioc;

import java.util.Optional;
import java.util.function.Function;

public interface ScopePrototype {
    Function<Object[], Object> get(String key);

    void put(String key, Function<Object[], Object> factory);

    void putArguments(String key, Object[] args);

    Optional<Object[]> getArguments(String key);
}
