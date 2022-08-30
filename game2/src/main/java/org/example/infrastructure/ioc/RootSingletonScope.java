package org.example.infrastructure.ioc;

import java.util.HashMap;
import java.util.Optional;

public class RootSingletonScope implements ScopeSingleton {
    private final HashMap<String, Object> dependencies = new HashMap<>();

    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(dependencies.get(key));
    }

    @Override
    public void put(String key, Object object) {
        dependencies.put(key, object);
    }
}
