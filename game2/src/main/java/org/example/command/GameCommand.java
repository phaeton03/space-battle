package org.example.command;

import org.example.asynchronous.ThreadStrategy;
import org.example.space_interface.Command;

public class GameCommand implements Command {
    private final ThreadStrategy threadStrategy;

    public GameCommand(ThreadStrategy threadStrategy) {
        this.threadStrategy = threadStrategy;
    }

    @Override
    public void execute() {
        new Thread(threadStrategy).start();
    }
}
