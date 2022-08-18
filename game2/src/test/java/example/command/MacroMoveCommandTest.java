package example.command;

import org.example.command.BurnFuelCommand;
import org.example.command.CheckFuelCommand;
import org.example.command.MacroCommand;
import org.example.command.MoveCommand;
import org.example.exception.FuelException;
import org.example.space_interface.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doThrow;

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
        macroMoveCommand.execute();

        verify(checkFuelCommand, times(1)).execute();
        verify(burnFuelCommand, times(1)).execute();
        verify(moveCommand, times(1)).execute();
    }

}