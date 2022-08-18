package org.example.infrastructure.ioc;

import java.util.Optional;

public interface ScopeSingleton {
    Optional<Object> get(String key);

    void put(String key, Object object);
}
