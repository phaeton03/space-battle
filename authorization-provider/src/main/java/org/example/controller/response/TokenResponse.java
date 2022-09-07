package org.example.controller.response;

import lombok.Value;

@Value
public class TokenResponse {
    String accessToken;
    String refreshToken;
}
