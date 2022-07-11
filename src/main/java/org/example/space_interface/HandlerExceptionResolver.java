package org.example.space_interface;

public interface HandlerExceptionResolver {
    Command handle(Command command, Exception exception);
}
