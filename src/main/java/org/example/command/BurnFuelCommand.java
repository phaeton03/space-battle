package org.example.command;

import org.example.space_interface.Burnable;
import org.example.space_interface.Command;

public class BurnFuelCommand implements Command {
    private final Burnable burnable;

    public BurnFuelCommand(Burnable burnable) {
        this.burnable = burnable;
    }

    @Override
    public void execute() {
        burnable.getBurn().burnFuel();
    }
}
