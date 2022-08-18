package org.example.domain;

import lombok.Getter;

@Getter
public class Burn {
    private final Fuel fuel;

    public Burn(Fuel fuel) {
        this.fuel = fuel;
    }

    public void burnFuel() {
        fuel.setFuel(fuel.getFuel() - fuel.getBurnRate());
    }
}
