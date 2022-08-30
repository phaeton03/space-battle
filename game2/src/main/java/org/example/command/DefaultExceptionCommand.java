package org.example.command;

import lombok.extern.slf4j.Slf4j;
import org.example.space_interface.Command;

@Slf4j
public class DefaultExceptionCommand implements Command {
    private Command command;
    private Exception exception;

    public DefaultExceptionCommand(Command command, Exception exception) {
        this.command = command;
        this.exception = exception;
    }


    @Override
    public void execute() {
        log.error("Command is {}, with exception is {}", command.getClass(), exception);
    }
}
