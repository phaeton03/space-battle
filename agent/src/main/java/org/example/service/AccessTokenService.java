package org.example.service;

import org.example.client.response.TokenResponse;

public interface AccessTokenService {
    TokenResponse getAccessTokenByRefresh(String refreshToken);
}
