package org.example.command;

import org.example.space_interface.Command;

public class DoubleRepeaterExceptionCommand implements Command {
    private final Command command;

    public DoubleRepeaterExceptionCommand(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {

    }
}
