package org.example.adapter;

import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.UObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DefaultInvocationHandler implements InvocationHandler {
    private final Class<?> adapterInterface;
    private final UObject uObject;

    public DefaultInvocationHandler(Class<?> adapterInterface, UObject uObject) {
        this.adapterInterface = adapterInterface;
        this.uObject = uObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        String iocResolveName = adapterInterface.getSimpleName() + "." + methodName;

        if (methodName.startsWith("get")) {

            return IoC.resolve(iocResolveName, uObject);
        }
        if (methodName.startsWith("set") && !methodName.equals("set")) {
            ((Command) IoC.resolve("SetProperty", uObject, methodName.split("^set")[1], args[0])).execute();

            return null;
        }

        if(method.getReturnType().equals(Void.TYPE)) {
            ((Command) IoC.resolve(iocResolveName)).execute();

            return null;
        }

        return method.invoke(uObject, args);
    }
}
