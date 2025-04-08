package com.example.ZCRPO.service;

import com.example.ZCRPO.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    User getByUsername(String username);

    UserDetailsService userDetailsService();
}