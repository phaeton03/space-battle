package org.example.command;

import org.example.adapter.DefaultInvocationHandler;
import org.example.domain.Vector;
import org.example.dto.GameDto;
import org.example.generator.Generator;
import org.example.infrastructure.ioc.IoC;
import org.example.infrastructure.ioc.Scope;
import org.example.space_interface.Command;
import org.example.space_interface.UObject;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

public class InterpretCommand implements Command {
    private final GameDto gameDto;

    private final static Map<String, Function<Object[], Object>> paramsFactory =
            Map.of("velocity", (args) -> new Vector((Integer) args[0], (Integer) args[1]));


    public InterpretCommand(GameDto gameDto) {
        this.gameDto = gameDto;
    }

    @Override
    public void execute() {
        Scope gameScope = IoC.resolve("Scope." + gameDto.getGameId());
        ((Command) IoC.resolve("Scope.Current", gameScope)).execute();

        UObject gameObject = IoC.resolve("GameObject", gameDto.getUObjectId());

        gameDto.getArgs().forEach((key, value) ->
                ((Command) IoC.resolve("SetProperty", gameObject, key, paramsFactory.get(key).apply(value)))
                        .execute());

        Class<?> commandClass;

        try {
            commandClass = Class.forName("org.example.command." + gameDto.getCommand());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object[] adaptersType = Arrays.stream(commandClass.getDeclaredFields())
                .map(Field::getType)
                .toArray();

        Object[] adapters = Arrays.stream(adaptersType)
                .map(type ->
                        ((Generator) IoC.resolve("Adapter",
                                type,
                                new DefaultInvocationHandler((Class<?>) type, gameObject))).execute())
                .toArray();

        Command command = IoC.resolve(gameDto.getCommand(), adapters);

        ((Queue<Command>) IoC.resolve("CommandQueue")).add(command);
    }
}
