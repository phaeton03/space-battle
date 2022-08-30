package org.example.command;

import lombok.RequiredArgsConstructor;
import org.example.space_interface.Command;
import org.example.space_interface.UObject;

@RequiredArgsConstructor
public class SetPropertyCommand implements Command {
    private final UObject uObject;
    private final String key;
    private final Object newValue;

    @Override
    public void execute() {
        uObject.setProperty(key, newValue);
    }
}
