package org.example.exception.handler;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.command.DefaultExceptionCommand;
import org.example.command.MacroCommand;
import org.example.config.ExceptionHandlerConfig;
import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@NoArgsConstructor
public class GlobalExceptionHandler implements HandlerExceptionResolver {
    private Queue<Command> commandQueue = new LinkedList<>();
    private final Map<Class<? extends Exception>, Class<? extends Command>> exceptionCommandHandlerMap
            = ExceptionHandlerConfig.exceptionCommandHandlerMap;

    private final Map<Class<? extends Command>, Class<? extends Command>> commandHandlerMap
            = ExceptionHandlerConfig.commandHandlerMap;

    public GlobalExceptionHandler(Queue<Command> commandQueue) {
        this.commandQueue = commandQueue;
    }

    public void handle(Command command, Exception e) {
        Class<? extends Command> commandExceptionDefinition =
                exceptionCommandHandlerMap.getOrDefault(e, DefaultExceptionCommand.class);

        Class<? extends Command> commandDefinition =
                commandHandlerMap.getOrDefault(command, commandExceptionDefinition);

        Command commandException = commandDefinition.equals(DefaultExceptionCommand.class)
                ? getDefaultExceptionCommand(command, e)
                : getExceptionCommand(commandDefinition, command);

        commandQueue.add(commandException);
    }

    @SneakyThrows
    private Command getExceptionCommand(Class<? extends Command> commandClass, Command command) {

        return commandClass.getConstructor(Command.class).newInstance(command);
    }

    @SneakyThrows
    private DefaultExceptionCommand getDefaultExceptionCommand(Command command, Exception ex) {

        return new DefaultExceptionCommand(command, ex);
    }
}
