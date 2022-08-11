package org.example.asynchronous;

import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;
import org.example.strategy.HandlerStrategy;

import java.util.Queue;

public class ThreadStrategy implements Runnable {
    private final Queue<Command> queue;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private HandlerStrategy handlerStrategy;

    private Boolean stop = false;

    public ThreadStrategy(Queue<Command> queue,
                          HandlerExceptionResolver handlerExceptionResolver,
                          HandlerStrategy handlerStrategy) {
        this.queue = queue;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.handlerStrategy = handlerStrategy;
    }

    @Override
    public void run() {
        while (!stop) {
            handlerStrategy.handle();
        }
    }

    public class HardStopCommand implements Command {
        @Override
        public void execute() {
            stop = true;
        }
    }

    public class SoftStopCommand implements Command {
        @Override
        public void execute() {

            handlerStrategy = () -> {
                while (!queue.isEmpty()) {
                    Command command = queue.poll();

                    try {
                        command.execute();
                    } catch (RuntimeException e) {
                        handlerExceptionResolver.handle(command, e);
                    }
                }
                stop = true;
            };
        }
    }
}
