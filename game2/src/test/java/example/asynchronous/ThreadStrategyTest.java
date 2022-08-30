package example.asynchronous;

import org.example.asynchronous.ThreadStrategy;
import org.example.command.GameCommand;
import org.example.exception.handler.GlobalExceptionHandler;
import org.example.infrastructure.ioc.IoC;
import org.example.infrastructure.ioc.Scope;
import org.example.space_interface.Command;
import org.example.strategy.DefaultThreadHandlerStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@ExtendWith(MockitoExtension.class)
public class ThreadStrategyTest {
    @Mock
    private Command command;

    @BeforeAll
    public static void register() {
        Scope newScope = IoC.resolve("Scope.New", (Scope) IoC.resolve("Scope.RootScope"));
        ((Command) IoC.resolve("Scope.Current", newScope)).execute();

        Queue<Command> commandQueue = new ConcurrentLinkedQueue<>();

        ((Command) IoC.resolve("IoC.RegisterFunction", "CommandQueue",
                (Function<Object[], Object>) args -> commandQueue)).execute();

        ((Command) IoC.resolve("IoC.Register", "HandlerExceptionResolver",
                GlobalExceptionHandler.class, "CommandQueue")).execute();

        ((Command) IoC.resolve("IoC.Register", "HandlerStrategy",
                DefaultThreadHandlerStrategy.class, "CommandQueue", "HandlerExceptionResolver")).execute();

        ((Command) IoC.resolve("IoC.Register", "GameCommand",
                GameCommand.class, "ThreadStrategy")).execute();
    }

    @AfterEach
    public void clear() {
        ((Queue<Command>) IoC.resolve("CommandQueue")).clear();
    }

    @BeforeEach
    public void init() {
        ((Command) IoC.resolve("IoC.RegisterSingleton", "ThreadStrategy",
                ThreadStrategy.class, "CommandQueue", "HandlerExceptionResolver", "HandlerStrategy")).execute();
    }

    @Test
    public void shouldSoftStop() {
        Queue<Command> commandQueue = IoC.resolve("CommandQueue");

        commandQueue.add(command);
        commandQueue.add(((ThreadStrategy) IoC.resolve("ThreadStrategy")).new SoftStopCommand());
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
