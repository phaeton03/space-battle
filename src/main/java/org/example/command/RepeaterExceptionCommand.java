package org.example.command;

import org.example.space_interface.Command;

public class RepeaterExceptionCommand implements Command {
    private final Command command;

    public RepeaterExceptionCommand(Command command) {
        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
