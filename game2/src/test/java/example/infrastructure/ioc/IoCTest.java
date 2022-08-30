package example.infrastructure.ioc;

import org.example.domain.Vector;
import org.example.infrastructure.ioc.IoC;
import org.example.infrastructure.ioc.Scope;
import org.example.space_interface.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;


@ExtendWith(MockitoExtension.class)
class IoCTest {

    @Test
    public void shouldGetIoCRegisterCommand() {
        IoC.RegisterCommand registerCommand = IoC.resolve("IoC.Register");

        assertInstanceOf(IoC.RegisterCommand.class, registerCommand);
    }

    @Test
    public void shouldRegisterInitialPosition() {
        Scope newScope = IoC.resolve("Scope.New", (Scope) IoC.resolve("Scope.RootScope"));
        ((Command) IoC.resolve("Scope.Current", newScope)).execute();

        IoC.RegisterCommand registerCommand = IoC.resolve("IoC.Register", "InitialPosition",
                Vector.class, 0, 0);

        registerCommand.execute();

        Vector initialPosition = IoC.resolve("InitialPosition");

        assertInstanceOf(Vector.class, initialPosition);
        assertThat(initialPosition.getX()).isEqualTo(0);
        assertThat(initialPosition.getY()).isEqualTo(0);
    }
}