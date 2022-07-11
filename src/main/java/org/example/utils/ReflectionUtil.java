package org.example.utils;

import lombok.SneakyThrows;
import org.example.command.DefaultExceptionCommand;
import org.example.command.DoubleRepeaterExceptionCommand;
import org.example.command.RepeaterExceptionCommand;
import org.example.space_interface.Command;

import java.util.Map;


public class ReflectionUtil {

    @SneakyThrows
    public static Command getExceptionCommand(Command command) {
        return command.getClass().getDeclaredConstructor(Command.class).newInstance(command);
    }

    @SneakyThrows
    public static DefaultExceptionCommand getDefaultExceptionCommand(Command command, Exception e) {
        return DefaultExceptionCommand.class
                .getDeclaredConstructor(Command.class, Exception.class)
                .newInstance(command, e);
    }
}
