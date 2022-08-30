package example.exception.handler;

import org.example.command.MoveCommand;
import org.example.exception.handler.GlobalExceptionHandler;
import org.example.space_interface.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Queue;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private final RuntimeException DEFAULT_EXCEPTION = new RuntimeException();

    @Mock
    private MoveCommand moveCommand;

    @Mock
    private Queue<Command> commandQueue;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldAddDefaultExceptionCommand() {
        given(commandQueue.add(any())).willReturn(true);
        globalExceptionHandler.handle(moveCommand, DEFAULT_EXCEPTION);

        verify(commandQueue, times(1)).add(any());
    }

}