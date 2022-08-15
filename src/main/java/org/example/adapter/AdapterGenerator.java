package org.example.adapter;

import org.example.space_interface.UObject;
import org.example.strategy.Generator;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.reflect.Proxy.*;

public class AdapterGenerator implements Generator {
    private final Class<?> adapterInterface;

    private final Map<String, Object> adapters = new HashMap<>();

    private final InvocationHandler invocationHandler;

    public AdapterGenerator(Class<?> adapterInterface, InvocationHandler invocationHandler) {
        this.adapterInterface = adapterInterface;
        this.invocationHandler = invocationHandler;
    }

    @Override
    public Object execute() {
        final String key = adapterInterface.getName();

        Optional.ofNullable(adapters.get(key))
                .orElse(adapters.put(key,
                        newProxyInstance(adapterInterface.getClassLoader(),
                                new Class[]{adapterInterface},
                                invocationHandler)));

        return adapters.get(key);
    }
}
