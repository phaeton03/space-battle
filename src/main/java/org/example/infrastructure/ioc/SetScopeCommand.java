package org.example.infrastructure.ioc;

import org.example.space_interface.Command;

public class SetScopeCommand implements Command {
    private final Scope scope;

    public SetScopeCommand(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void execute() {
        ((Command) IoC.resolve("Scope.SetCurrent", scope)).execute();
    }
}
