package org.example.infrastructure.ioc;

import org.example.space_interface.Command;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;


public class IoC {

    private static final ThreadLocal<Scope> scopesPrototype = new ThreadLocal<>();
    private static final ThreadLocal<ScopeSingleton> scopesSingleton = new ThreadLocal<>();

    static {
        Scope rootScope = new RootScope();
        RootSingletonScope rootScopeSingleTone = new RootSingletonScope();
        rootScopeSingleTone.put("Scope.RootSingleton", rootScopeSingleTone);
        rootScope.put("Scope.RootScope", (args) -> rootScope);
        rootScope.put("IoC.Register", (args) -> new RegisterCommand(args));
        rootScope.put("IoC.RegisterFunction", (args) ->
                new RegisterFunctionCommand((String) args[0], (Function<Object[], Object>) args[1]));
        rootScope.put("IoC.RegisterSingleton", (args) -> new RegisterSingletonCommand(args));
        rootScope.put("Scope.New", (args) -> new BasicScope((Scope) args[0]));
        rootScope.put("Scope.Current", (args) -> new SetCurrentScopeCommand((Scope) args[0]));

        scopesPrototype.set(rootScope);
        scopesSingleton.set(rootScopeSingleTone);
    }

    private static IoCStrategy currentStrategy = new DefaultIoCStrategy();

    public static <T> T resolve(String key, Object... args) {

        Scope currentScope = scopesPrototype.get();
        ScopeSingleton currentScopeSingleton = scopesSingleton.get();

        return (T) currentScopeSingleton.get(key)
                .orElseGet(() -> (T) currentScope.get(key)
                .apply(currentStrategy.execute(currentScope.getArguments(key).orElse(args))));
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
            final Scope currentScope = scopesPrototype.get();

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
            final Scope currentScope = scopesPrototype.get();

            currentScope.put(dependency, func);
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
            scopesPrototype.set(currentScope);
        }
    }
}
