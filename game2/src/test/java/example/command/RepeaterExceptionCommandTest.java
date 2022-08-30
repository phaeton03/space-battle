package example.command;

import org.example.command.MoveCommand;
import org.example.command.RepeaterExceptionCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class RepeaterExceptionCommandTest {
    @Mock
    private MoveCommand moveCommand;

    @InjectMocks
    private RepeaterExceptionCommand repeaterExceptionCommand;

    @Test
    void shouldRepeatMoveCommand() {
        doNothing().when(moveCommand).execute();

        repeaterExceptionCommand.execute();

        verify(moveCommand, times(1)).execute();
    }
}