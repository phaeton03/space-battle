package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.AuthProviderClient;
import org.example.utils.dto.ClaimsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {
    @Value("${token.public}")
    private String publicKey;

    private final AuthProviderClient authProviderClient;

    public ClaimsDto validate(String accessToken, String refreshToken) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(KeysParser.getPublicKeyFromString(publicKey))
                .build();

        Claims claims;
        ClaimsDto claimsDto;

        try {
            claims = jwtParser.parseClaimsJws(accessToken).getBody();
            claimsDto = new ClaimsDto(claims, accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("Token is Expired");

            String newAccessToken = authProviderClient.getAccessTokenByRefreshToken(refreshToken).getAccessToken();
            log.info("Token was refreshed");
            claims = jwtParser.parseClaimsJws(newAccessToken).getBody();
            claimsDto = new ClaimsDto(claims, newAccessToken);
        }

        return claimsDto;
    }
}
