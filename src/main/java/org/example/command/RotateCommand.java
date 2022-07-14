package org.example.command;

import org.example.space_interface.Command;
import org.example.space_interface.Rotable;


public class RotateCommand implements Command {
    private final Rotable rotable;

    public RotateCommand(Rotable rotable) {
        this.rotable = rotable;
    }

    @Override
    public void execute() {
        rotable.getDirection().next(rotable.getAngularVelocity());
    }
}
