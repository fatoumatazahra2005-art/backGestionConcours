package org.example.ges_concours.Controller;

import lombok.RequiredArgsConstructor;
import org.example.ges_concours.Dto.AuthResponse;
import org.example.ges_concours.Dto.LoginRequest;
import org.example.ges_concours.Dto.RegisterRequest;
import org.example.ges_concours.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}