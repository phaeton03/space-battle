package org.example.command;

import org.example.domain.Direction;
import org.example.domain.Vector;
import org.example.space_interface.Command;
import org.example.space_interface.Movable;
import org.example.space_interface.Rotable;

public class ChangeVelocityDirectionCommand implements Command {
    private final Movable movable;
    private final Rotable rotable;

    public ChangeVelocityDirectionCommand(Movable movable, Rotable rotable) {
        this.movable = movable;
        this.rotable = rotable;
    }

    @Override
    public void execute() {
        Vector velocity = movable.getVelocity();
        Direction direction = rotable.getDirection();

        final double angle = (2 * Math.PI * direction.getDirection()) / direction.getDirectionNumber();
        final double moduleVector = Math.sqrt(Math.pow(velocity.getX(), 2) + Math.pow(velocity.getY(), 2));

        velocity.setX(Math.toIntExact(Math.round(moduleVector * Math.cos(angle))));
        velocity.setY(Math.toIntExact(Math.round(moduleVector * Math.sin(angle))));

        Integer x = velocity.getX();
        Integer y = velocity.getY();
    }
}
