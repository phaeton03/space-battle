package org.example.service;

import org.example.dto.GameDto;

import java.util.List;

public interface GameInfoEmitterService {
    void addGameCommandByUser(GameDto gameDto);

    void addGameCommandListByUser(List<GameDto> gameDtoList);

}
