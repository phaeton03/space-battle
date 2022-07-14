package org.example.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.exception.FuelException;

@Getter
@Setter
public class Fuel {
    private Integer fuel;
    private Integer burnRate;

    public Fuel(Integer fuel, Integer burnRate) {
        this.fuel = fuel;
        this.burnRate = burnRate;
    }

    public void isFuelEnough() {
        int fuelRemain = fuel - burnRate;

        if (fuelRemain < 0) {
            throw new FuelException();
        }

        System.out.println("Fuel remains = " + fuelRemain);
    }
}
