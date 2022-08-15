package org.example.command;

import org.example.space_interface.Command;
import org.example.strategy.HandlerStrategy;

public class HardStop implements Command {
    HandlerStrategy handlerStrategy;

    @Override
    public void execute() {

    }
}
