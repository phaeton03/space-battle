package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    @GetMapping("/admin")
    public String getInfo() {
        return "Hello admin! It's an agent microservice.";
    }
}
