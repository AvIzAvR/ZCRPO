package com.example.ZCRPO.service.impl;

import com.example.ZCRPO.exception.EmailInUseException;
import com.example.ZCRPO.exception.RoleNotFoundException;
import com.example.ZCRPO.exception.UsernameTakenException;
import com.example.ZCRPO.model.User;
import com.example.ZCRPO.model.dto.request.SignInRequest;
import com.example.ZCRPO.model.dto.response.JwtAuthenticationResponse;
import com.example.ZCRPO.model.dto.response.SignUpRequest;
import com.example.ZCRPO.model.role.Role;
import com.example.ZCRPO.model.role.RoleEnum;
import com.example.ZCRPO.repository.RoleRepository;
import com.example.ZCRPO.repository.UserRepository;
import com.example.ZCRPO.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserServiceImpl userService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenServiceImpl refreshTokenService;

    public AuthenticationServiceImpl(UserServiceImpl userService, JwtServiceImpl jwtService,
                                     PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                                     UserRepository userRepository, RoleRepository roleRepository,
                                     RefreshTokenServiceImpl refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameTakenException("Error: username is taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailInUseException("Error: email already in use!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role roleUser = roleRepository.findByName(RoleEnum.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException("Error. Role user not found."));
        List<Role> roles = new ArrayList<>();
        roles.add(roleUser);
        user.setRoles(roles);

        userRepository.save(user);

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.generateRefreshToken(user);
        return new JwtAuthenticationResponse("Success!", request.getUsername(), jwt, refreshToken);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        refreshTokenService.deleteRefreshToken((User) user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.generateRefreshToken(user);
        return new JwtAuthenticationResponse("Success", request.getUsername(), jwt, refreshToken);
    }
}