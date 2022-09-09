package org.example.exception.handler;

import lombok.SneakyThrows;
import org.example.command.DefaultExceptionCommand;
import org.example.config.ExceptionHandlerConfig;
import org.example.space_interface.Command;
import org.example.space_interface.HandlerExceptionResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;


public class GlobalExceptionHandler implements HandlerExceptionResolver {
    private final Queue<Command> commandQueue;

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

        Class<? extends Command> commandDefinition;

        /**
         * Коллекция commandHanlderMap не принимает null в качестве ключа.
         * **/
        if (command != null) {
            commandDefinition = commandHandlerMap.getOrDefault(command, commandExceptionDefinition);
        } else {
            commandDefinition = commandExceptionDefinition;
        }

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
