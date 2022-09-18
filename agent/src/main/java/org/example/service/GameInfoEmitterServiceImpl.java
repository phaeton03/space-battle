package org.example.service;

import org.example.dto.GameDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameInfoEmitterServiceImpl implements GameInfoEmitterService {
    private final GameDto movableDto = GameDto.builder()
            .gameId("Game1")
            .command("MoveCommand")
            .uObjectId(548L)
            .args(Map.of("velocity", new Object[]{1,1}))
            .build();

    private final GameDto rotateDto = GameDto.builder()
            .gameId("Game1")
            .command("FinishMovableCommand")
            .uObjectId(548L)
            .build();

    private final Queue<GameDto> gameDtoQueue = new LinkedList<>();

    public GameInfoEmitterServiceImpl(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

        gameDtoQueue.add(movableDto);
        gameDtoQueue.add(rotateDto);
    }

    @Value("${app.main-exchange}")
    private String MAIN_EXCHANGE_NAME;

    @Value("${app.routing-key}")
    private String GAME_COMMAND_QUEUE;

    private final AmqpTemplate rabbitTemplate;

//    @Scheduled(initialDelay = 3000, fixedRate = 1000000000)
    public void emitGameCommands() {
        System.out.println("emit");
        if (!gameDtoQueue.isEmpty()) {
            GameDto gameDto = gameDtoQueue.poll();
            rabbitTemplate.convertAndSend(MAIN_EXCHANGE_NAME, GAME_COMMAND_QUEUE, gameDto);
        }else {
            rabbitTemplate.convertAndSend(MAIN_EXCHANGE_NAME, GAME_COMMAND_QUEUE, movableDto);
        }
    }

    public void addGameCommandByUser(GameDto gameDto) {
        gameDtoQueue.add(gameDto);
    }

    public void addGameCommandListByUser(List<GameDto> gameDtoList) {
        gameDtoQueue.addAll(gameDtoList);
    }
}
