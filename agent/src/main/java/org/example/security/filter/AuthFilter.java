package org.example.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.client.AuthProviderClient;
import org.example.client.response.TokenResponse;
import org.example.constants.AuthConstants;
import org.example.utils.JwtUtils;
import org.example.utils.dto.ClaimsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.constants.AuthConstants.*;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var header = request.getHeader(AUTH);
        log.info("Dont filter with header = {}", header);

        if (header == null)
            return true;

        return !header.startsWith(BEARER);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("AuthentificationFilter: {}", request.getHeader(AUTH));

        String accessToken = request.getHeader(AUTH).substring(BEARER.length());
        String refreshToken = request.getHeader(REFRESH);

        ClaimsDto claimsDto;

        try {
            claimsDto = jwtUtils.validate(accessToken, refreshToken);
        } catch (Exception e) {
            log.info("Exception {}", e.getMessage());
            setErrorResponse(HttpStatus.FORBIDDEN, response, e);
            return;
        }

        @SuppressWarnings("unchecked")
        List<String> roles = claimsDto.getClaims().get("roles", List.class);

        log.info("AuthFilter: Roles {}", roles);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(r -> new SimpleGrantedAuthority(ROLE + r.toUpperCase()))
                .collect(Collectors.toList());

        SecurityContextHolder
                .getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(claimsDto.getClaims().getSubject(),
                        claimsDto.getToken(),
                        authorities));

        log.info("AuthFilter set Authentification");

        filterChain.doFilter(request, response);
    }

    @SneakyThrows
    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        log.error("Auth error", ex);
        response.getWriter().format("{\"error\": \"%s\"}", ex.getMessage());
    }
}
