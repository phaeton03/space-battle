package example.command;

import org.example.command.RotateCommand;
import org.example.domain.Direction;
import org.example.exception.AngularVelocityException;
import org.example.exception.DirectionException;
import org.example.space_interface.Rotable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RotateCommandTest {
    private final static Integer DIRECTION_NUMBER = 8;
    private static final Direction START_DIRECTION = new Direction(4, DIRECTION_NUMBER);

    private static final Integer ANGULAR_VELOCITY = 7;


    private static final Direction END_DIRECTION = new Direction(3, DIRECTION_NUMBER);

    @Mock
    private Rotable rotable;

    @InjectMocks
    private RotateCommand rotateCommand;

    @Test
    void shouldThrowAngularVelocityException() {
        given(rotable.getAngularVelocity()).willThrow(AngularVelocityException.class);

        assertThatThrownBy(() -> rotateCommand.execute()).isInstanceOf(AngularVelocityException.class);
    }

    @Test
    void shouldThrowDirectionException() {
        given(rotable.getDirection()).willThrow(DirectionException.class);

        assertThatThrownBy(() -> rotateCommand.execute()).isInstanceOf(DirectionException.class);
    }

    @Test
    void shouldTurnToTheRightDirection() {
        given(rotable.getDirection()).willReturn(START_DIRECTION);
        given(rotable.getAngularVelocity()).willReturn(ANGULAR_VELOCITY);

        rotateCommand.execute();

        assertThat(rotable.getDirection().getDirection()).isEqualTo(END_DIRECTION.getDirection());
    }
}
