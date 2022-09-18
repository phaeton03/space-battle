package org.example.client;

import feign.Param;
import org.example.client.response.TokenResponse;
import org.example.constants.AuthConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth-provider", url = "localhost:8082", path = "/authenticate")
public interface AuthProviderClient {
    @GetMapping("/refresh")
    TokenResponse getAccessTokenByRefreshToken(@RequestParam String refreshToken);
}
