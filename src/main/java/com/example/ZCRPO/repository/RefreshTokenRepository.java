package com.example.ZCRPO.repository;

import com.example.ZCRPO.model.RefreshToken;
import com.example.ZCRPO.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteById(Long id);

    boolean existsByUser(User user);
}