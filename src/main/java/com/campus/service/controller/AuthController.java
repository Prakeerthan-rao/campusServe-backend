package com.campus.service.controller;

import com.campus.service.dto.LoginRequest;
import com.campus.service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRequest.Response> login(@Valid @RequestBody LoginRequest.Request request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
