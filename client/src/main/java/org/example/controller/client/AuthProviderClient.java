package org.example.controller.client;

import org.example.controller.client.response.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-provider", url = "localhost:8082", path = "/authenticate")
public interface AuthProviderClient {
    @GetMapping("/access")
    TokenResponse getAccessToken(@RequestParam String userName, @RequestParam String password);

    @GetMapping("/refresh")
    TokenResponse getAccessTokenByRefreshToken(@RequestParam String refreshToken);
}
