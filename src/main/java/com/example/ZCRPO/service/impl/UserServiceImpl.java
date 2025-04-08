package com.example.ZCRPO.service.impl;

import com.example.ZCRPO.model.User;
import com.example.ZCRPO.repository.UserRepository;
import com.example.ZCRPO.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

    }

    public User getById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}