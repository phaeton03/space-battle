package org.example.utils.dto;

import io.jsonwebtoken.Claims;
import lombok.Value;

@Value
public class ClaimsDto {
    Claims claims;
    String token;
}
