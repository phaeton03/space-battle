package org.example.command;

import org.example.domain.Burn;
import org.example.domain.Fuel;
import org.example.domain.Vector;
import org.example.exception.FuelException;
import org.example.space_interface.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MacroMoveCommandTest {

    @Mock
    private BurnFuelCommand burnFuelCommand;

    @Mock
    private CheckFuelCommand checkFuelCommand;

    @Mock
    private MoveCommand moveCommand;

    private final List<Command> commandList = new ArrayList<>();

    private final MacroCommand macroMoveCommand = new MacroCommand(commandList);

    @BeforeEach
    public void init() {
        commandList.add(checkFuelCommand);
        commandList.add(burnFuelCommand);
        commandList.add(moveCommand);
    }

    @Test
    void shouldReturnByFuelException() {
        doThrow(FuelException.class).when(checkFuelCommand).execute();

        assertThatThrownBy(macroMoveCommand::execute).isInstanceOf(FuelException.class);
    }

    @Test
    void shouldCallAllCommands() {
        doNothing().when(checkFuelCommand).execute();
        doNothing().when(burnFuelCommand).execute();
        doNothing().when(moveCommand).execute();

        macroMoveCommand.execute();

        verify(checkFuelCommand, times(1)).execute();
        verify(burnFuelCommand, times(1)).execute();
        verify(moveCommand, times(1)).execute();
    }

}