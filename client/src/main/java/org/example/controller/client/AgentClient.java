package org.example.controller.client;

import feign.Headers;
import feign.Param;
import org.example.controller.client.request.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static org.example.controller.constants.AuthConstants.AUTH;
import static org.example.controller.constants.AuthConstants.REFRESH;

@FeignClient(value = "agent", url = "localhost:8081")
public interface AgentClient {
    @GetMapping("/admin")
    String getInfo(@RequestHeader(AUTH) String accessToken,
                   @RequestHeader(REFRESH) String refreshToken);

    @PostMapping("/command")
    void addGameCommand(@RequestHeader(AUTH) String accessToken,
                        @RequestHeader(REFRESH) String refreshToken,
                        @RequestBody GameDto gameDto);

    @PostMapping("/command-list")
    void addGameCommandList(@RequestHeader(AUTH) String accessToken,
                            @RequestHeader(REFRESH) String refreshToken,
                            @RequestBody List<GameDto> gameDtoList);
}
