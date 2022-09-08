package org.example.client;

import org.example.client.response.TokenResponse;
import org.example.constants.AuthConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "auth-provider", url = "localhost:8082", path = "/authenticate")
public interface AuthProviderClient {
    @GetMapping("/refresh")
    TokenResponse getAccessTokenByRefreshToken(String refreshToken);
}
