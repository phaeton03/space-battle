package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.GameDto;
import org.example.service.GameInfoEmitterService;
import org.example.service.GameInfoEmitterServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameInfoEmitterService gameInfoEmitterService;

    @PostMapping("/command")
    public void addGameCommand(@RequestBody GameDto gameDto) {

        gameInfoEmitterService.addGameCommandByUser(gameDto);
    }

    @PostMapping("/command-list")
    public void addGameCommandList(@RequestBody List<GameDto> gameDtoList) {
        gameInfoEmitterService.addGameCommandListByUser(gameDtoList);
    }
}
