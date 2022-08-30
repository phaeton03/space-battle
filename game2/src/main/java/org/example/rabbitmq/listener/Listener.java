package org.example.rabbitmq.listener;


import lombok.extern.slf4j.Slf4j;
import org.example.dto.GameDto;
import org.example.infrastructure.ioc.IoC;
import org.example.space_interface.Command;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Listener {

    @RabbitListener(queues = "${app.queue-game}")
    public void makeCommand(GameDto gameDto) {
        System.out.println(gameDto.getGameId());

        try {
            ((Command) IoC.resolve("InterpretCommand", gameDto)).execute();
        } catch (Exception e) {
            log.debug(e.toString());
        }
    }
}
