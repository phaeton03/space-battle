package org.example.handler;

import org.example.exception.TokenIsEmptyException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RequestHandler {
    public String getJwtFromStringRequest(String request) {
        if (StringUtils.hasText(request) && request.startsWith("Bearer ")) {
            return request.substring(7);
        }
        throw new TokenIsEmptyException("Jwt is empty or Bearer missing");
    }
}
