package org.example.config;

import com.google.common.collect.Maps;
import org.example.command.DefaultExceptionCommand;
import org.example.command.DoubleRepeaterExceptionCommand;
import org.example.command.RepeaterExceptionCommand;
import org.example.space_interface.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import static java.util.stream.Stream.*;

public class ExceptionHandlerConfig {
    public static final Map<Class<? extends Exception>, Class<? extends Command>> exceptionCommandHandlerMap
            = Map.of(Exception.class, RepeaterExceptionCommand.class,
            NullPointerException.class, DefaultExceptionCommand.class);
    public static final Map<Class<? extends Command>, Class<? extends Command>> commandHandlerMap
            = Map.of(RepeaterExceptionCommand.class, DoubleRepeaterExceptionCommand.class,
            DoubleRepeaterExceptionCommand.class, DefaultExceptionCommand.class);
}
