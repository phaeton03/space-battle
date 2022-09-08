package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.client.AgentClient;
import org.example.controller.client.AuthProviderClient;
import org.example.controller.client.request.GameDto;
import org.example.controller.client.response.TokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.example.controller.constants.AuthConstants.BEARER;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final AuthProviderClient authProviderClient;
    private final AgentClient agentClient;

    private TokenResponse tokenResponse;

    @GetMapping("/queryToken")
    public TokenResponse queryToken(String user, String password) {
        tokenResponse = authProviderClient.getAccessToken(user, password);

        return tokenResponse;
    }

    @GetMapping("/refreshToken")
    public TokenResponse queryTokenByRefreshToken(String refreshToken) {
        tokenResponse = authProviderClient.getAccessTokenByRefreshToken(refreshToken);

        return tokenResponse;
    }

    @PostMapping("/command")
    public void addGameCommand(@RequestBody GameDto gameDto) {
        agentClient.addGameCommand(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), gameDto);
    }

    @PostMapping("/command-list")
    public void addGameCommandList(@RequestBody List<GameDto> gameDtoList) {
        agentClient.addGameCommandList(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), gameDtoList);
    }

    @GetMapping("/admin")
    public String getAdminInfo(){
        return agentClient.getInfo(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
    }
}
