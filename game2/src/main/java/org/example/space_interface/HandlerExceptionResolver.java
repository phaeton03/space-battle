package org.example.space_interface;

public interface HandlerExceptionResolver {
    void handle(Command command, Exception exception);
}
