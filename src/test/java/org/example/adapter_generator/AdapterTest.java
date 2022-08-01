package org.example.adapter_generator;

import org.example.adapter.AdapterGenerator;
import org.example.adapter.DefaultInvocationHandler;
import org.example.command.SetPropertyCommand;
import org.example.domain.Vector;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.Movable;
import org.example.space_interface.UObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AdapterTest {
    @Mock
    private UObject uObject;

    private final Vector POSITION = new Vector(10,0);

    private final Vector NEW_POSITION = new Vector(100,100);

    private final String PROPERTY_POSITION = "Position";

    @BeforeEach
    public void register() {
        ((Command) IoC.resolve("IoC.Register", "Adapter",
                AdapterGenerator.class)).execute();

        ((Command) IoC.resolve("IoC.RegisterFunction",
                "Movable.getPosition",
                (Function<Object[], Object>) (args) -> (((UObject) args[0]).getProperty("position")))).execute();

        ((Command) IoC.resolve("IoC.Register",
                "SetProperty", SetPropertyCommand.class)).execute();
    }

    @Test
    public void shouldGetPositionWithAdapter() {
        given(uObject.getProperty("position")).willReturn(POSITION);

        AdapterGenerator adapterGenerator =
                IoC.resolve("Adapter", Movable.class, new DefaultInvocationHandler(Movable.class, uObject));

        Movable movableAdapter = (Movable) adapterGenerator.execute();

        assertThat(movableAdapter.getPosition()).usingRecursiveComparison().isEqualTo(POSITION);
    }

    @Test
    public void shouldSetPositionWithAdapter() {
        given(uObject.getProperty("position")).willReturn(POSITION);
        
        AdapterGenerator adapterGenerator =
                IoC.resolve("Adapter", Movable.class, new DefaultInvocationHandler(Movable.class, uObject));

        Movable movableAdapter = (Movable) adapterGenerator.execute();

        movableAdapter.setPosition(NEW_POSITION);

        Vector position = movableAdapter.getPosition();


        verify(uObject, times(1)).setProperty(PROPERTY_POSITION, NEW_POSITION);
    }
}
