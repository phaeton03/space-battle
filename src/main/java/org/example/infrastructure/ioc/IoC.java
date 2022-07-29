package org.example.infrastructure.ioc;

import org.example.exception.ArgumentsNotFoundException;
import org.example.space_interface.Command;
import org.example.utils.ReflectionUtil;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;


public class IoC {

    private static final ThreadLocal<Scope> scopes = new ThreadLocal<>();

    static {
        RootScope rootScope = new RootScope();
        rootScope.put("IoC.Register", (args) -> new IoC.RegisterCommand(args));
        rootScope.put("Scope.New", (args) -> new BasicScope((Scope) args[0]));
        rootScope.put("Scope.Current", (args) -> new SetCurrentScopeCommand((Scope) args[0]));
        scopes.set(rootScope);
    }

    private static IoCStrategy currentStrategy = new DefaultIoCStrategy();

    public static <T> T resolve(String key, Object... args) {
        Scope currentScope = scopes.get();

        return (T) currentScope.get(key).apply(currentStrategy.execute(currentScope.getArguments(key).orElse(args)));
    }

    public static class RegisterCommand implements Command {
        private final Object[] params;

        public RegisterCommand(Object... params) {
            this.params = params;
        }

        /**
         * Нулевой параметр args[0] - имя зависимости.
         * args[1] - тип объекта
         * args[2] до args.length - параметры конструктора
         **/
        @Override
        public void execute() {
            Constructor<?>[] declaredConstructors = ((Class<?>) params[1]).getDeclaredConstructors();
            final Scope currentScope = scopes.get();

            currentScope.put(params[0].toString(),
                    (param) -> getInstance(getConstructor(declaredConstructors), param));

            if (params.length > 2) {
                currentScope.putArguments(params[0].toString(), Arrays.copyOfRange(params, 2, params.length));
            }
        }

        private Object getInstance(Constructor<?> constructor, Object... params) {
            try {
                return constructor.newInstance(params);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private Constructor<?> getConstructor(Constructor<?>[] declaredConstructors) {
            try {
                return ((Class<?>) params[1]).getDeclaredConstructor();

            } catch (NoSuchMethodException e) {

                return Arrays.stream(declaredConstructors)
                        .filter(c -> c.getParameterCount() > 0)
                        .findFirst()
                        .get();
            }
        }

    }


    public static class SetupStrategyCommand implements Command {
        private IoCStrategy ioCStrategy;

        public SetupStrategyCommand(IoCStrategy ioCStrategy) {
            this.ioCStrategy = ioCStrategy;
        }

        @Override
        public void execute() {
            currentStrategy = ioCStrategy;
        }
    }

    public static class SetCurrentScopeCommand implements Command {
        Scope currentScope;

        public SetCurrentScopeCommand(Scope currentScope) {
            this.currentScope = currentScope;
        }

        @Override
        public void execute() {
            scopes.set(currentScope);
        }
    }
}
