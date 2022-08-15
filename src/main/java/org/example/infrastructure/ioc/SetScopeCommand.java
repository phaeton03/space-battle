package org.example.infrastructure.ioc;

import org.example.space_interface.Command;

public class SetScopeCommand implements Command {
    private final ScopePrototype scopePrototype;

    public SetScopeCommand(ScopePrototype scopePrototype) {
        this.scopePrototype = scopePrototype;
    }

    @Override
    public void execute() {
        ((Command) IoC.resolve("Scope.SetCurrent", scopePrototype)).execute();
    }
}
