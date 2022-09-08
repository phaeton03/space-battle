package org.example.asynchronous;

import lombok.RequiredArgsConstructor;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;
import org.example.state.CommandState;
import org.example.strategy.DefaultThreadHandlerStrategy;
import org.example.strategy.HandlerStrategy;

import java.util.Queue;

public class ThreadStrategy implements Runnable {
    private final Queue<Command> queue;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private HandlerStrategy handlerStrategy;

    private CommandState commandState = new DefaultCommandState();

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
        while (!stop || commandState != null) {
            commandState.run();
        }

        stop = false;
    }

    public class HardStopCommand implements Command {
        @Override
        public void execute() {
            stop = true;
            commandState = null;
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

    public class RunCommand implements Command {
        @Override
        public void execute() {
            commandState = new DefaultCommandState();
        }
    }

    public class MoveToCommand implements Command {
        @Override
        public void execute() {
            commandState = new ReserveQueueCommandState();
        }
    }

    class DefaultCommandState implements CommandState {

        @Override
        public void run() {
            handlerStrategy = IoC.resolve("DefaultThreadHandlerStrategy");

            handlerStrategy.handle();
        }
    }

    class ReserveQueueCommandState implements CommandState {

        @Override
        public void run() {
            Queue<Command> reserveCommandQueue = IoC.resolve("ReserveCommandQueue");

            reserveCommandQueue.addAll(queue);
            queue.clear();
            stop = true;
        }
    }
}
