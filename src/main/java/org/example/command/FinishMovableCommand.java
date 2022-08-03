package org.example.command;

import org.example.space_interface.Command;

public class FinishMovableCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Command is finished");
    }
}
