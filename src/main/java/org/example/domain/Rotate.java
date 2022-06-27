package org.example.domain;

import org.example.space_interface.Rotable;


public class Rotate {
    private final Rotable rotable;

    public Rotate(Rotable rotable) {
        this.rotable = rotable;
    }

    public void execute() {
        rotable.getDirection().next(rotable.getAngularVelocity());
    }
}
