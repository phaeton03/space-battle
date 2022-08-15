package org.example.infrastructure.ioc;

import org.example.domain.Vector;
import org.example.space_interface.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
public class IoCTestParallel {

    private final Vector PLUS_VECTOR = new Vector(10, 10);
    private final Vector NEGATE_VECTOR = new Vector(-10, -10);

    @BeforeEach
    public void setScope() {
        ScopePrototype newScopePrototype = IoC.resolve("Scope.New", (ScopePrototype) IoC.resolve("Scope.RootScope"));
        ((Command) IoC.resolve("Scope.Current", newScopePrototype)).execute();
    }

    @Test
    public void shouldPlusVector() {
        ((Command) IoC.resolve("IoC.Register", "InitialPosition",
                Vector.class, 0, 0)).execute();

        Vector initialPosition = IoC.resolve("InitialPosition");

        initialPosition.plus(PLUS_VECTOR);

        assertThat(initialPosition.getX()).isEqualTo(10);
        assertThat(initialPosition.getY()).isEqualTo(10);
    }

    @Test
    public void shouldNegateVector() {
        ((Command) IoC.resolve("IoC.Register", "InitialPosition",
                Vector.class, 10, 10)).execute();

        Vector initialPosition = IoC.resolve("InitialPosition", 10, 10);

        initialPosition.plus(NEGATE_VECTOR);

        assertThat(initialPosition.getX()).isEqualTo(0);
        assertThat(initialPosition.getY()).isEqualTo(0);
    }

}
