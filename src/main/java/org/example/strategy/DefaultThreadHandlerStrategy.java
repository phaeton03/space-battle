package org.example.strategy;

import org.example.command.FinishMovableCommand;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;

import java.util.Queue;

public class DefaultThreadHandlerStrategy implements HandlerStrategy {
    private final Queue<Command> queue;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public DefaultThreadHandlerStrategy(Queue<Command> queue, HandlerExceptionResolver handlerExceptionResolver) {
        this.queue = queue;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void handle() {
        System.out.println(queue.size());
            Command command = queue.poll();

            try {
                command.execute();
            } catch (RuntimeException e) {
                handlerExceptionResolver.handle(command, e);
            }
    }
}
