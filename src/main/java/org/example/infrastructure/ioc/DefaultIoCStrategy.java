package org.example.infrastructure.ioc;

import org.example.exception.NotFoundException;

import java.util.Arrays;
import java.util.Optional;

import static org.example.infrastructure.ioc.IoC.resolve;

public class DefaultIoCStrategy implements IoCStrategy {

    @Override
    public Object[] execute(Object[] params) {
        return Arrays.stream(params)
                .map((obj) -> {
                    try {
                        return resolve(obj.toString());
                    } catch (NotFoundException e) {
                        return obj;
                    }
                }).toArray();
    }
}
