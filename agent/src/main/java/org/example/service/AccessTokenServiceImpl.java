package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.client.AuthProviderClient;
import org.example.client.response.TokenResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {
    private final AuthProviderClient authProviderClient;

    @Override
    public TokenResponse getAccessTokenByRefresh(String refreshToken) {
        return authProviderClient.getAccessTokenByRefreshToken(refreshToken);
    }
}
