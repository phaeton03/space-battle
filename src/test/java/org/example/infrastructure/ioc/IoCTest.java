package org.example.infrastructure.ioc;

import org.example.domain.Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.internal.MockedStaticImpl;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class IoCTest {

    @Test
    public void shouldGetIoCRegisterCommand() {
        IoC.RegisterCommand registerCommand = IoC.resolve("IoC.Register");

        assertInstanceOf(IoC.RegisterCommand.class, registerCommand);
    }

    @Test
    public void shouldRegisterInitialPosition() {
        IoC.RegisterCommand registerCommand = IoC.resolve("IoC.Register", "InitialPosition",
                Vector.class, 0, 0);


        registerCommand.execute();

        Vector initialPosition = IoC.resolve("InitialPosition");

        assertInstanceOf(Vector.class, initialPosition);
        assertThat(initialPosition.getX()).isEqualTo(0);
        assertThat(initialPosition.getY()).isEqualTo(0);
    }
}