package org.example.client;

import org.example.client.response.TokenResponse;
import org.example.constants.AuthConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient
public interface AuthProviderClient {
    @GetMapping("/access")
    TokenResponse getAccessToken(String userName, String password);

    @GetMapping("/refresh")
    TokenResponse getAccessTokenByRefreshToken(@RequestHeader(AuthConstants.AUTH) String refreshToken);
}
