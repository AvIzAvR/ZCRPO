package com.example.ZCRPO.controller;

import com.example.ZCRPO.model.dto.request.RefreshTokenRequest;
import com.example.ZCRPO.model.dto.response.JwtAuthenticationResponse;
import com.example.ZCRPO.service.impl.RefreshTokenServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refresh")
public class RefreshController {

    private final RefreshTokenServiceImpl refreshTokenService;

    public RefreshController(RefreshTokenServiceImpl refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        JwtAuthenticationResponse jwtResponse = refreshTokenService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(jwtResponse);
    }
}