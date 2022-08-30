package example.command;

import org.example.command.ChangeVelocityDirectionCommand;
import org.example.command.MacroCommand;
import org.example.command.RotateCommand;
import org.example.space_interface.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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