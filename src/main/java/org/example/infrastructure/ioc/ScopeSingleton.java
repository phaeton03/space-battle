package org.example.infrastructure.ioc;

import java.util.Optional;
import java.util.function.Function;

public interface ScopeSingleton {
    Optional<Object> get(String key);

    void put(String key, Object object);
}
