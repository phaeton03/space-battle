package example.command;

import org.example.command.CheckFuelCommand;
import org.example.domain.Fuel;
import org.example.exception.FuelException;
import org.example.space_interface.FuelCheckable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CheckFuelCommandTest {
    private static final Fuel NOT_ENOUGH_FUEL = new Fuel(100, 500);
    private static final Fuel FUEL = new Fuel(100, 10);

    @Mock
    private FuelCheckable fuelCheckable;

    @InjectMocks
    private CheckFuelCommand checkFuelCommand;

    @Test
    void shouldThrowFuelException() {
        given(fuelCheckable.getFuel()).willReturn(NOT_ENOUGH_FUEL);

        assertThatThrownBy(() -> checkFuelCommand.execute()).isInstanceOf(FuelException.class);
    }

    @Test
    void shoulVerifyGetFuel() {
        given(fuelCheckable.getFuel()).willReturn(FUEL);
        checkFuelCommand.execute();

        verify(fuelCheckable, times(1)).getFuel();
    }
}