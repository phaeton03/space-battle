package org.example.infrastructure.ioc;

import org.example.exception.NotFoundException;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class RootScope implements Scope {
    private final HashMap<String, Function<Object[], Object>> dependencies = new HashMap<>();
    private final HashMap<String, Object[]> registerArguments = new HashMap<>();

    @Override
    public Function<Object[], Object> get(String key) {
        System.out.println(key);
        return Optional.ofNullable(dependencies.get(key)).orElseThrow(NotFoundException::new);
    }

    @Override
    public void put(String key, Function<Object[], Object> factory) {
        dependencies.put(key, factory);
    }

    @Override
    public void putArguments(String key, Object[] args) {
        registerArguments.put(key, args);
    }

    @Override
    public Optional<Object[]> getArguments(String key) {
        return Optional.ofNullable(registerArguments.get(key));
    }
}
