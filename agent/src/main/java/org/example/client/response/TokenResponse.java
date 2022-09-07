package org.example.client.response;

import lombok.Value;

@Value
public class TokenResponse {
    String accessToken;
    String refreshToken;
}
