package org.example.infrastructure.ioc;

import org.example.exception.NotFoundException;

import java.util.Arrays;

public class DefaultIoCStrategy implements IoCStrategy {

    @Override
    public Object[] execute(Object[] params) {
        return Arrays.stream(params)
                .map((obj) -> {
                    try {
                        return IoC.resolve(obj.toString());
                    } catch (NotFoundException e) {
                        return obj;
                    }
                }).toArray();
    }
}
