package org.example.security;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.response.TokenResponse;
import org.example.domain.User;
import org.example.exception.AuthException;
import org.example.handler.RequestHandler;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final UserService userService;

    private final RequestHandler requestHandler;

    @Value("${token.private}")
    private String privateKey;

    @Value("${token.expired}")
    private Long validityInMilliseconds;

    public TokenResponse generateToken(String userName, String password) {
        User user = userService.findUserByNameAndPassword(userName, password);

        Claims claims = Jwts.claims();
        claims.put("roles", user.getRoles());
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + validityInMilliseconds);

        String accessToken = Jwts.builder()
                .setSubject(user.getName())
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .setIssuer("SPACE-BATTLE/OTUS")
                .signWith(SignatureAlgorithm.RS256, privateKey.trim())
                .compact();

        String refreshToken = createRefreshToken(userName);

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse generateTokenByRefreshToken(String refreshToken) {
        String authRefreshToken = requestHandler.getJwtFromStringRequest(refreshToken);
        validateToken(authRefreshToken);
        String userName = getUserNameFromToken(authRefreshToken);

        User user = userService.findUserByName(userName);

        return generateToken(user.getName(), user.getPassword());
    }

    public String createRefreshToken(String name) {
        Date now = new Date();
        Date validity = new GregorianCalendar(2300, Calendar.FEBRUARY, 1).getTime();
        Claims claims = Jwts.claims().setSubject(name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, privateKey.trim())
                .compact();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(privateKey.trim()).parseClaimsJws(token);

        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException("JWT token is expired or invalid");
        }
    }

    public String getUserNameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(privateKey.trim()).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
}
