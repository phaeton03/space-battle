package org.example.asynchronous;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;
import org.example.strategy.HandlerStrategy;

import java.util.Queue;
import java.util.function.Function;

public class ThreadStrategy implements Runnable {
    private final Queue<Command> queue;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private HandlerStrategy handlerStrategy;

    public ThreadStrategy() {
        this.queue = IoC.resolve("CommandQueue");
        this.handlerExceptionResolver = IoC.resolve("HandlerExceptionResolver");
        this.handlerStrategy = IoC.resolve("HandlerStrategy");
    }

    @Override
    public void run() {
        handlerStrategy.handle();
    }

    public class HardStopCommand implements Command {
        @Override
        public void execute() {
            handlerStrategy = () -> {};
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
           };
        }
    }
}
