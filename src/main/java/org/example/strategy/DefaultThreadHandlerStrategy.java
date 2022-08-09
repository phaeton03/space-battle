package org.example.strategy;

import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;

import java.util.Queue;

public class DefaultThreadHandlerStrategy implements HandlerStrategy {
    private final Queue<Command> queue;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public DefaultThreadHandlerStrategy() {
        this.queue = IoC.resolve("CommandQueue");
        this.handlerExceptionResolver = IoC.resolve("HandlerExceptionResolver");
    }

    @Override
    public void handle() {
        while (!queue.isEmpty()) {
            Command command = queue.poll();

            try {
                command.execute();
            } catch (RuntimeException e) {
                handlerExceptionResolver.handle(command, e);
            }
        }
    }
}
