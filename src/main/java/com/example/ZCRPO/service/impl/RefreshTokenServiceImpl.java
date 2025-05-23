package com.example.ZCRPO.service.impl;

import com.example.ZCRPO.exception.InvalidRefreshTokenException;
import com.example.ZCRPO.model.RefreshToken;
import com.example.ZCRPO.model.User;
import com.example.ZCRPO.model.dto.response.JwtAuthenticationResponse;
import com.example.ZCRPO.repository.RefreshTokenRepository;
import com.example.ZCRPO.service.RefreshTokenService;
import com.example.ZCRPO.utils.TokenGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final JwtServiceImpl jwtService;
    private static final String INVALID_REFRESH_TOKEN = "Invalid refresh token!";

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, TokenGenerator tokenGenerator, JwtServiceImpl jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.jwtService = jwtService;
    }

    public String generateRefreshToken(UserDetails userDetails) {
        User user = (User) userDetails;
        if (refreshTokenRepository.existsByUser(user)) {
            deleteRefreshToken(user);
        }
        String token = tokenGenerator.generateToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(Instant.now().plusMillis(60 * 60 * 24 * 7 * (long)1000));
        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public boolean isRefreshTokenValid(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            return !refreshToken.getExpiryDate().isBefore(Instant.now());
        }
        return false;
    }

    public UserDetails getUserDetailsFromRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new InvalidRefreshTokenException(INVALID_REFRESH_TOKEN));
    }

    public String generateTokenFromRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .map(t -> jwtService.generateToken(t.getUser()))
                .orElseThrow(() -> new InvalidRefreshTokenException(INVALID_REFRESH_TOKEN));
    }

    public JwtAuthenticationResponse refreshToken(String refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new InvalidRefreshTokenException(INVALID_REFRESH_TOKEN);
        }

        UserDetails userDetails = getUserDetailsFromRefreshToken(refreshToken);
        String jwt = generateTokenFromRefreshToken(refreshToken);
        deleteRefreshToken((User) userDetails);
        String newRefreshToken = generateRefreshToken(userDetails);

        return new JwtAuthenticationResponse("Success", userDetails.getUsername(), jwt, newRefreshToken);
    }

    public void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}