package org.example.infrastructure.ioc;

import org.example.exception.NotFoundException;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

public class BasicScope implements Scope {
    private final HashMap<String, Function<Object[], Object>> holder = new HashMap<>();
    private final HashMap<String, Object[]> registerArguments = new HashMap<>();
    private final Scope parentScope;

    public BasicScope(Scope parentScope) {
        this.parentScope = parentScope;
    }

    @Override
    public Function<Object[], Object> get(String key) {

        return Optional.ofNullable(holder.get(key)).orElseGet(() -> parentScope.get(key));
    }

    @Override
    public void put(String key, Function<Object[], Object> factory) {
        holder.put(key, factory);
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
