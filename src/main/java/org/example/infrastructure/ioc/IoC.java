package org.example.infrastructure.ioc;

import org.example.space_interface.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;


public class IoC {

    private static final ThreadLocal<ScopePrototype> scopesPrototype = new ThreadLocal<>();
    private static final ThreadLocal<ScopeSingleton> scopesSingleton = new ThreadLocal<>();

    static {
        ScopePrototype rootScopePrototype = new RootScopePrototype();
        RootSingletonScope rootScopeSingleTone = new RootSingletonScope();
        rootScopeSingleTone.put("Scope.RootSingleton", rootScopeSingleTone);
        rootScopePrototype.put("Scope.RootScope", (args) -> rootScopePrototype);
        rootScopePrototype.put("IoC.Register", (args) -> new IoC.RegisterCommand(args));
        rootScopePrototype.put("IoC.RegisterFunction", (args) ->
                new IoC.RegisterFunctionCommand((String) args[0], (Function<Object[], Object>) args[1]));
        rootScopePrototype.put("IoC.RegisterSingleton", (args) -> new IoC.RegisterSingletonCommand(args));
        rootScopePrototype.put("Scope.New", (args) -> new BasicScopePrototype((ScopePrototype) args[0]));
        rootScopePrototype.put("Scope.Current", (args) -> new SetCurrentScopeCommand((ScopePrototype) args[0]));

        scopesPrototype.set(rootScopePrototype);
        scopesSingleton.set(rootScopeSingleTone);
    }

    private static IoCStrategy currentStrategy = new DefaultIoCStrategy();

    public static <T> T resolve(String key, Object... args) {

        ScopePrototype currentScopePrototype = scopesPrototype.get();
        ScopeSingleton currentScopeSingleton = scopesSingleton.get();

        return (T) currentScopeSingleton.get(key)
                .orElseGet(() -> (T) currentScopePrototype.get(key)
                .apply(currentStrategy.execute(currentScopePrototype.getArguments(key).orElse(args))));
//        return (T) currentScopePrototype.get(key)
//                .apply(currentStrategy.execute(currentScopePrototype.getArguments(key).orElse(args)));
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
            final ScopePrototype currentScopePrototype = scopesPrototype.get();

            currentScopePrototype.put(params[0].toString(),
                    (param) -> getInstance(getConstructor(declaredConstructors), param));

            if (params.length > 2) {
                currentScopePrototype.putArguments(params[0].toString(), Arrays.copyOfRange(params, 2, params.length));
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

    public static class RegisterSingletonCommand implements Command {
        private final Object[] params;

        public RegisterSingletonCommand(Object... params) {
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
            final ScopeSingleton currentScopeSingleton = scopesSingleton.get();

            currentScopeSingleton.put(params[0].toString(),
                    getInstance(getConstructor(declaredConstructors), Arrays.copyOfRange(params, 2, params.length)));

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

    public static class RegisterFunctionCommand implements Command {
        private final Function<Object[], Object> func;
        private final String dependency;

        public RegisterFunctionCommand(String dependency, Function<Object[], Object> func) {
            this.dependency = dependency;
            this.func = func;
        }

        @Override
        public void execute() {
            final ScopePrototype currentScopePrototype = scopesPrototype.get();

            currentScopePrototype.put(dependency, func);
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
        ScopePrototype currentScopePrototype;

        public SetCurrentScopeCommand(ScopePrototype currentScopePrototype) {
            this.currentScopePrototype = currentScopePrototype;
        }

        @Override
        public void execute() {
            scopesPrototype.set(currentScopePrototype);
        }
    }
}
