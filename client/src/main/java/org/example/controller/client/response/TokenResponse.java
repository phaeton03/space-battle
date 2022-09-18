package org.example.controller.client.response;

import lombok.Value;

import static org.example.controller.constants.AuthConstants.BEARER;

@Value
public class TokenResponse {
    String accessToken;
    String refreshToken;

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = BEARER + accessToken;
        this.refreshToken = BEARER + refreshToken;
    }
}
