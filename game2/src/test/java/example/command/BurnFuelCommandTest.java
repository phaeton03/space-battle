package example.command;

import org.example.command.BurnFuelCommand;
import org.example.domain.Burn;
import org.example.domain.Fuel;
import org.example.space_interface.Burnable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BurnFuelCommandTest {
    private static final Fuel FUEL = new Fuel(100, 10);

    private static final Burn BURN = new Burn(FUEL);

    private static final Fuel FUEL_REMAIN = new Fuel(90, 10);

    @Mock
    private Burnable burnable;

    @InjectMocks
    private BurnFuelCommand burnFuelCommand;

    @Test
    void shouldBurnFuel() {
        given(burnable.getBurn()).willReturn(BURN);

        burnFuelCommand.execute();

        assertThat(BURN.getFuel().getFuel()).isEqualTo(FUEL_REMAIN.getFuel());
    }
}