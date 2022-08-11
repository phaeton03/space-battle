package org.example.asynchronous;

import org.example.command.FinishMovableCommand;
import org.example.command.GameCommand;
import org.example.exception.handler.GlobalExceptionHandler;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.strategy.DefaultThreadHandlerStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ThreadStrategyTest {
    @Mock
    private Command command;

    @BeforeAll
    public static void register() {
        Queue<Command> commandQueue = new ConcurrentLinkedQueue<>();

        ((Command) IoC.resolve("IoC.RegisterFunction", "CommandQueue",
                (Function<Object[], Object>) args -> commandQueue)).execute();

        ((Command) IoC.resolve("IoC.Register", "HandlerExceptionResolver",
                GlobalExceptionHandler.class, "CommandQueue")).execute();

        ((Command) IoC.resolve("IoC.Register", "HandlerStrategy",
                DefaultThreadHandlerStrategy.class, "CommandQueue", "HandlerExceptionResolver")).execute();

        ((Command) IoC.resolve("IoC.RegisterSingleton", "ThreadStrategy",
                ThreadStrategy.class, "CommandQueue", "HandlerExceptionResolver", "HandlerStrategy")).execute();

        ((Command) IoC.resolve("IoC.Register", "GameCommand",
                GameCommand.class, "ThreadStrategy")).execute();
    }

    @Test
    public void shouldSoftStop() {
        Queue<Command> commandQueue = IoC.resolve("CommandQueue");
        commandQueue.add(command);
        commandQueue.add(((ThreadStrategy)IoC.resolve("ThreadStrategy")).new SoftStopCommand());
        commandQueue.add(command);
        commandQueue.add(command);

       ((ThreadStrategy) IoC.resolve("ThreadStrategy")).run();

        verify(command, times(3)).execute();
    }

    @Test
    public void shouldHardStop() {
        Queue<Command> commandQueue = IoC.resolve("CommandQueue");
        commandQueue.add(command);
        commandQueue.add(((ThreadStrategy)IoC.resolve("ThreadStrategy")).new HardStopCommand());
        commandQueue.add(command);
        commandQueue.add(command);

        ((ThreadStrategy) IoC.resolve("ThreadStrategy")).run();

        verify(command, times(1)).execute();
    }
}
