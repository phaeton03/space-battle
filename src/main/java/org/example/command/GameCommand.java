package org.example.command;

import org.example.asynchronous.ThreadStrategy;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;

public class GameCommand implements Command {
    private final ThreadStrategy threadStrategy;

    public GameCommand() {
        this.threadStrategy = IoC.resolve("ThreadStrategy");
    }

    @Override
    public void execute() {
        new Thread(threadStrategy).start();
    }
}
