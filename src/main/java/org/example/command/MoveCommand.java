package org.example.command;

import org.example.space_interface.Command;
import org.example.space_interface.Movable;

public class MoveCommand implements Command {
    private final Movable movable;

    public MoveCommand(Movable movable) {
        this.movable = movable;
    }

    @Override
    public void execute() {
        movable.getPosition().plus(movable.getVelocity());
    }
}
