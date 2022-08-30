package example.command;

import org.example.command.MoveCommand;
import org.example.domain.Vector;
import org.example.exception.PositionChangeException;
import org.example.exception.PositionException;
import org.example.exception.VelocityException;
import org.example.space_interface.Movable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class MoveCommandTest {
    private static final Vector START_POSITION = new Vector(12, 5);
    private static final Vector END_POSITION = new Vector(5, 8);
    private static final Vector VELOCITY = new Vector(-7,3);

    private static final Vector POSITION_OVERFLOW_MAX = new Vector(Integer.MAX_VALUE - 100, 150);

    private static final Vector VELOCITY_OVERFLOW_MAX_1 = new Vector(Integer.MAX_VALUE - 100, Integer.MAX_VALUE - 100);

    private static final Vector VELOCITY_OVERFLOW_MAX_2 = new Vector(Integer.MAX_VALUE - 100, Integer.MAX_VALUE / 2 - 100);

    private static final Vector POSITION_OVERFLOW_MIN = new Vector(Integer.MIN_VALUE + 100, 150);

    private static final Vector VELOCITY_OVERFLOW_MIN_1 = new Vector(Integer.MIN_VALUE + 100, Integer.MIN_VALUE + 100);

    private static final Vector VELOCITY_OVERFLOW_MIN_2 = new Vector(Integer.MIN_VALUE + 100, Integer.MIN_VALUE / 2 + 100);

    @Mock
    private Movable movable;

    @InjectMocks
    private MoveCommand moveCommand;

    @Test
    void shouldThrowPositionException() {
        given(movable.getPosition()).willThrow(PositionException.class);

        assertThatThrownBy(() -> moveCommand.execute()).isInstanceOf(PositionException.class);
    }

    @Test
    void shouldThrowVelocityException() {
        given(movable.getVelocity()).willThrow(VelocityException.class);

        assertThatThrownBy(() -> moveCommand.execute()).isInstanceOf(VelocityException.class);
    }

    @Test
    void shouldThrowPositionChangeExceptionMaxOverFlowTest1() {
        given(movable.getVelocity()).willReturn(VELOCITY_OVERFLOW_MAX_1);
        given(movable.getPosition()).willReturn(POSITION_OVERFLOW_MAX);

        assertThatThrownBy(() -> moveCommand.execute()).isInstanceOf(PositionChangeException.class);
    }

    @Test
    void shouldThrowPositionChangeExceptionMaxOverFlowTest2() {
        given(movable.getVelocity()).willReturn(VELOCITY_OVERFLOW_MAX_2);
        given(movable.getPosition()).willReturn(POSITION_OVERFLOW_MAX);

        assertThatThrownBy(() -> moveCommand.execute()).isInstanceOf(PositionChangeException.class);
    }

    @Test
    void shouldThrowPositionChangeExceptionMinOverFlowTest1() {
        given(movable.getVelocity()).willReturn(VELOCITY_OVERFLOW_MIN_1);
        given(movable.getPosition()).willReturn(POSITION_OVERFLOW_MIN);

        assertThatThrownBy(() -> moveCommand.execute()).isInstanceOf(PositionChangeException.class);
    }

    @Test
    void shouldThrowPositionChangeExceptionMinOverFlowTest2() {
        given(movable.getVelocity()).willReturn(VELOCITY_OVERFLOW_MIN_2);
        given(movable.getPosition()).willReturn(POSITION_OVERFLOW_MIN);

        assertThatThrownBy(() -> moveCommand.execute()).isInstanceOf(PositionChangeException.class);
    }

    @Test
    void shouldGetToTheRightPosition() {
        given(movable.getVelocity()).willReturn(VELOCITY);
        given(movable.getPosition()).willReturn(START_POSITION);

        moveCommand.execute();

        assertThat(movable.getPosition().getX()).isEqualTo(END_POSITION.getX());
        assertThat(movable.getPosition().getY()).isEqualTo(END_POSITION.getY());
    }


}