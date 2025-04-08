package com.example.ZCRPO.service;

import com.example.ZCRPO.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface RefreshTokenService {

    String generateRefreshToken(UserDetails userDetails);

    boolean isRefreshTokenValid(String token);

    UserDetails getUserDetailsFromRefreshToken(String refreshToken);

    String generateTokenFromRefreshToken(String refreshToken);

    void deleteRefreshToken(User user);
}