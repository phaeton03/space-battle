package org.example.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Value
@Builder
@Jacksonized
public class GameDto {
    String gameId;
    Long uObjectId;
    String command;
    Map<String, Object[]> args;
}
