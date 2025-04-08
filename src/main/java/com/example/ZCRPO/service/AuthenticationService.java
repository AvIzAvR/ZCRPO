package com.example.ZCRPO.service;


import com.example.ZCRPO.model.dto.request.SignInRequest;
import com.example.ZCRPO.model.dto.response.JwtAuthenticationResponse;
import com.example.ZCRPO.model.dto.response.SignUpRequest;

public interface AuthenticationService {

    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}