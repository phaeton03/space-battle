package org.example.command;

import org.example.space_interface.Command;

import java.util.List;

public class MacroCommand implements Command {
    private final List<Command> commandList;

    public MacroCommand(List<Command> commandList) {
        this.commandList = commandList;
    }

    @Override
    public void execute() {
        commandList.forEach(Command::execute);
    }
}
