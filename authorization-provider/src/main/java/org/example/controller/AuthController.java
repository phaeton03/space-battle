package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.constants.AuthConstants;
import org.example.controller.response.TokenResponse;
import org.example.security.JwtTokenProvider;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticate")
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/access")
    public TokenResponse auth(String userName, String password) {

        return jwtTokenProvider.generateToken(userName, password);
    }

    @GetMapping("/refresh")
    public TokenResponse auth(@RequestHeader(AuthConstants.AUTH_KEY) String refreshToken) {

        return jwtTokenProvider.generateTokenByRefreshToken(refreshToken);
    }

}
