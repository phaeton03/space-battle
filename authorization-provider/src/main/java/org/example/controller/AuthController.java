package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.constants.AuthConstants;
import org.example.controller.response.TokenResponse;
import org.example.security.JwtTokenProvider;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authenticate")
@Slf4j
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/access")
    public TokenResponse auth(@RequestParam String userName, @RequestParam String password) {
        log.info("start authentification");
        return jwtTokenProvider.generateToken(userName, password);
    }

    @GetMapping("/refresh")
    public TokenResponse auth(@RequestParam String refreshToken) {

        return jwtTokenProvider.generateTokenByRefreshToken(refreshToken);
    }


}
