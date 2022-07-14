package org.example.command;

import org.example.exception.FuelException;
import org.example.space_interface.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MacroRotableCommandTest {
    @Mock
    private ChangeVelocityDirectionCommand changeVelocityDirectionCommand;

    @Mock
    private RotateCommand rotateCommand;

    private final List<Command> commandList = new ArrayList<>();

    private final MacroCommand macroRotateCommand = new MacroCommand(commandList);

    @BeforeEach
    public void init() {
        commandList.add(rotateCommand);
        commandList.add(changeVelocityDirectionCommand);
    }

    @Test
    void shouldCallAllCommands() {
        doNothing().when(rotateCommand).execute();
        doNothing().when(changeVelocityDirectionCommand).execute();

        macroRotateCommand.execute();

        verify(rotateCommand, times(1)).execute();
        verify(changeVelocityDirectionCommand, times(1)).execute();
    }

}