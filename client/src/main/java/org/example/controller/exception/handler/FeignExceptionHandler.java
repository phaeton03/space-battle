package org.example.controller.exception.handler;

import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestControllerAdvice
public class FeignExceptionHandler {
    @ExceptionHandler(FeignException.Forbidden.class)
    public String handleFeignUnauthorizedException(FeignException e, HttpServletResponse response) {
        response.setStatus(e.status());

        return e.contentUTF8();
    }
}
