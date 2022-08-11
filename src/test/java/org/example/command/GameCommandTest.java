package org.example.command;

import org.example.adapter.AdapterGenerator;
import org.example.asynchronous.ThreadStrategy;
import org.example.exception.handler.GlobalExceptionHandler;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.example.space_interface.UObject;
import org.example.strategy.DefaultThreadHandlerStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameCommandTest {
    @Mock
    private ThreadStrategy threadStrategy;

    @InjectMocks
    private GameCommand gameCommand;

    @Test
    public void shouldLaunchGame() {
        gameCommand.execute();

        verify(threadStrategy, times(1)).run();
    }

}