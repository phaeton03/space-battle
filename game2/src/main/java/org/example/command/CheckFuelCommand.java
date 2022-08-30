package org.example.command;

import org.example.space_interface.Command;
import org.example.space_interface.FuelCheckable;

public class CheckFuelCommand implements Command {
    private final FuelCheckable fuelCheckable;

    public CheckFuelCommand(FuelCheckable fuelCheckable) {
        this.fuelCheckable = fuelCheckable;
    }

    @Override
    public void execute() {
        fuelCheckable.getFuel().isFuelEnough();
    }
}
