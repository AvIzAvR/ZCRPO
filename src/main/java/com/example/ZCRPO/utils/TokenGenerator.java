package com.example.ZCRPO.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGenerator {

    public String generateToken() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 6);
    }

}