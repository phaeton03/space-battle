package org.example.command;

import org.example.exception.handler.GlobalExceptionHandler;
import org.example.space_interface.Command;

import java.util.Queue;

public class MacroCommand implements Command {
    private final Queue<Command> commandQueue;

    private final GlobalExceptionHandler globalExceptionHandler;

    public MacroCommand(Queue<Command> commandQueue, GlobalExceptionHandler globalExceptionHandler) {
        this.commandQueue = commandQueue;
        this.globalExceptionHandler = globalExceptionHandler;
    }

    @Override
    public void execute() {
        while (!commandQueue.isEmpty()) {
            Command cmd = commandQueue.poll();
            try {
                cmd.execute();
            } catch (Exception e) {
                commandQueue.add(globalExceptionHandler.handle(cmd, e));
            }
        }
    }
}
