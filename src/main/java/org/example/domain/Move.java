package org.example.domain;

import org.example.space_interface.Movable;

public class Move {
    private final Movable movable;

    public Move(Movable movable) {
        this.movable = movable;
    }

    public void execute() {
        movable.getPosition().plus(movable.getVelocity());
    }
}
