package org.example.command;

import org.example.domain.Direction;
import org.example.domain.Vector;
import org.example.space_interface.Movable;
import org.example.space_interface.Rotable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChangeVelocityDirectionCommandTest {
    private static final Vector START_VELOCITY = new Vector(1, 0);
    private static final Vector END_VELOCITY_45 = new Vector(1, 1);
    private static final Vector END_VELOCITY_90 = new Vector(0, 1);
    private static final Vector END_VELOCITY_225 = new Vector(-1, -1);

    private static final Direction DIRECTION_45 = new Direction(1, 8);

    private static final Direction DIRECTION_90 = new Direction(2, 8);

    private static final Direction DIRECTION_225 = new Direction(5, 8);

    @Mock
    private Movable movable;

    @Mock
    private Rotable rotable;

    @InjectMocks
    private ChangeVelocityDirectionCommand changeVelocityDirectionCommand;

    @ParameterizedTest
    @MethodSource("getEndVelocity")
    void shouldCorrectChangeVelocityDirection(Vector endVelocity, Direction direction ) {
        given(movable.getVelocity()).willReturn(START_VELOCITY);
        given(rotable.getDirection()).willReturn(direction);

        changeVelocityDirectionCommand.execute();

        assertThat(movable.getVelocity()).isEqualToComparingFieldByField(endVelocity);
    }

    static Stream<Arguments> getEndVelocity() {

        return Stream.of(
                arguments(END_VELOCITY_45, DIRECTION_45),
                arguments(END_VELOCITY_90, DIRECTION_90),
                arguments(END_VELOCITY_225, DIRECTION_225)
        );
    }
}