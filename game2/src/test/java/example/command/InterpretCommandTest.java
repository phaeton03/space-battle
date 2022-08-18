package example.command;

import org.example.adapter.AdapterGenerator;
import org.example.command.InterpretCommand;
import org.example.command.MoveCommand;
import org.example.command.SetPropertyCommand;
import org.example.dto.GameDto;
import org.example.infrastructure.ioc.IoC;
import org.example.infrastructure.ioc.Scope;
import org.example.space_interface.Command;
import org.example.space_interface.UObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InterpretCommandTest {
    private static final String GAME_ID = "Game1";
    private final Long UOBJECT_ID = 100L;

    private final GameDto GAME_MOVE_COMMAND_DTO = GameDto.builder()
            .gameId(GAME_ID)
            .command("MoveCommand")
            .uObjectId(UOBJECT_ID)
            .args(Map.of("velocity", new Object[]{1, 1}))
            .build();

    @Mock
    private UObject uObject;

    private final InterpretCommand interpretCommand = new InterpretCommand(GAME_MOVE_COMMAND_DTO);

    private static Queue<Command> commandQueue = new LinkedList<>();

    @BeforeAll
    public static void register() {
        Scope newScope = IoC.resolve("Scope.New", (Scope) IoC.resolve("Scope.RootScope"));

        ((Command) IoC.resolve("IoC.Register", "Adapter",
                AdapterGenerator.class)).execute();

        ((Command) IoC.resolve("IoC.RegisterFunction", "Scope." + GAME_ID,
                (Function<Object[], Object>) (args) -> newScope)).execute();

        ((Command) IoC.resolve("Scope.Current", "Scope." + GAME_ID)).execute();

        ((Command) IoC.resolve("IoC.RegisterFunction",
                "Movable.getPosition",
                (Function<Object[], Object>) (args) -> (((UObject) args[0]).getProperty("position")))).execute();

        ((Command) IoC.resolve("IoC.Register",
                "SetProperty", SetPropertyCommand.class)).execute();

        ((Command) IoC.resolve("IoC.Register",
                "MoveCommand", MoveCommand.class)).execute();

        ((Command) IoC.resolve("IoC.RegisterFunction",
                "CommandQueue",
                (Function<Object[], Object>) (args) -> commandQueue)).execute();
    }

    @Test
    public void shouldInterpretCommandAndAddMoveCommand() {
        ((Command) IoC.resolve("IoC.RegisterFunction",
                "GameObject",
                (Function<Object[], Object>) (args) -> uObject)).execute();

        interpretCommand.execute();

        assertThat(commandQueue.size()).isEqualTo(1);
        assertThat(commandQueue.poll()).isExactlyInstanceOf(MoveCommand.class);
    }
}