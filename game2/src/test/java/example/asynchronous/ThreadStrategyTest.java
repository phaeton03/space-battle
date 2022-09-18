package example.asynchronous;

import org.example.asynchronous.ThreadStrategy;
import org.example.command.GameCommand;
import org.example.exception.handler.GlobalExceptionHandler;
import org.example.infrastructure.ioc.IoC;
import org.example.infrastructure.ioc.Scope;
import org.example.space_interface.Command;
import org.example.state.CommandState;
import org.example.strategy.DefaultThreadHandlerStrategy;
import org.example.strategy.HandlerStrategy;
import org.example.utils.ReflectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ThreadStrategyTest {
    @Mock
    private Command command;

    @Mock
    private ThreadStrategy.RunCommand runCommand;

    private static HandlerStrategy handlerStrategy;

    @BeforeAll
    public static void register() {
        Scope newScope = IoC.resolve("Scope.New", (Scope) IoC.resolve("Scope.RootScope"));
        ((Command) IoC.resolve("Scope.Current", newScope)).execute();

        Queue<Command> commandQueue = new ConcurrentLinkedQueue<>();

        Queue<Command> reserveCommandQueue = new ConcurrentLinkedQueue<>();

        ((Command) IoC.resolve("IoC.RegisterFunction", "CommandQueue",
                (Function<Object[], Object>) args -> commandQueue)).execute();

        ((Command) IoC.resolve("IoC.RegisterFunction", "ReserveCommandQueue",
                (Function<Object[], Object>) args -> reserveCommandQueue)).execute();

        ((Command) IoC.resolve("IoC.Register", "HandlerExceptionResolver",
                GlobalExceptionHandler.class, "CommandQueue")).execute();

        ((Command) IoC.resolve("IoC.Register", "HandlerStrategy",
                DefaultThreadHandlerStrategy.class, "CommandQueue", "HandlerExceptionResolver")).execute();

        handlerStrategy = IoC.resolve("HandlerStrategy");

        ((Command) IoC.resolve("IoC.Register", "GameCommand",
                GameCommand.class, "ThreadStrategy")).execute();

    }

    @AfterEach
    public void clear() {
        ((Queue<Command>) IoC.resolve("CommandQueue")).clear();
        ((ThreadStrategy) IoC.resolve("ThreadStrategy")).resetToDefaultState();
    }

    @BeforeEach
    public void init() {
        ((Command) IoC.resolve("IoC.RegisterSingleton", "ThreadStrategy",
                ThreadStrategy.class, "CommandQueue", "HandlerExceptionResolver", handlerStrategy)).execute();
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

    @Test
    public void shouldMoveToCommand() {
        Queue<Command> commandQueue = IoC.resolve("CommandQueue");

        commandQueue.add(command);
        commandQueue.add(((ThreadStrategy) IoC.resolve("ThreadStrategy")).new MoveToCommand());
        commandQueue.add(command);
        commandQueue.add(command);

        ((ThreadStrategy) IoC.resolve("ThreadStrategy")).run();

        Queue<Command> reserveCommandQueue = IoC.resolve("ReserveCommandQueue");

        assertThat(commandQueue).isEmpty();
        assertThat(reserveCommandQueue).hasSize(2);
    }

    @Test
    public void shouldRunCommand() {
        Queue<Command> commandQueue = IoC.resolve("CommandQueue");

        commandQueue.add(runCommand);
        commandQueue.add(((ThreadStrategy) IoC.resolve("ThreadStrategy")).new HardStopCommand());

        ((ThreadStrategy) IoC.resolve("ThreadStrategy")).run();

        verify(runCommand, times(1)).execute();
    }
}
