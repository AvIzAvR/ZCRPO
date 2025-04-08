package com.example.ZCRPO.controller;

import com.example.ZCRPO.model.dto.request.SignInRequest;
import com.example.ZCRPO.model.dto.response.JwtAuthenticationResponse;
import com.example.ZCRPO.model.dto.response.SignUpRequest;
import com.example.ZCRPO.service.impl.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationServiceImpl authenticationService;

    public AuthController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signUp(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        JwtAuthenticationResponse jwtResponse = authenticationService.signIn(request);
        return ResponseEntity.ok(jwtResponse);
    }
}