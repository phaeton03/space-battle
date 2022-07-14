package org.example.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoubleRepeaterExceptionCommandTest {
    @Mock
    private MoveCommand moveCommand;

    @InjectMocks
    private DoubleRepeaterExceptionCommand doubleRepeaterExceptionCommand;

    @Test
    void shouldRepeatMoveCommand() {
        doNothing().when(moveCommand).execute();

        doubleRepeaterExceptionCommand.execute();

        verify(moveCommand, times(1)).execute();
    }

}