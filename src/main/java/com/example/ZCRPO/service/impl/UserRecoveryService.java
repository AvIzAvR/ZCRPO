package com.example.ZCRPO.service.impl;

import com.example.ZCRPO.exception.CodeExpiredException;
import com.example.ZCRPO.exception.InvalidCodeException;
import com.example.ZCRPO.model.Code;
import com.example.ZCRPO.model.User;
import com.example.ZCRPO.model.dto.request.ConfirmRecovery;
import com.example.ZCRPO.model.dto.request.RecoveryRequest;
import com.example.ZCRPO.repository.CodeRepository;
import com.example.ZCRPO.repository.UserRepository;
import com.example.ZCRPO.service.EmailSender;
import com.example.ZCRPO.utils.TokenGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserRecoveryService {
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final TokenGenerator tokenGenerator;
    private final PasswordEncoder passwordEncoder;

    public UserRecoveryService(UserRepository userRepository, CodeRepository codeRepository, TokenGenerator tokenGenerator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    public void recoveryRequest(RecoveryRequest recoveryRequest) {
        userRepository.findByEmail(recoveryRequest.getEmail()).ifPresent(user -> {
            codeRepository.deleteByUserId(user.getId());
            String confirmationCode = tokenGenerator.generateToken();
            EmailSender.sendConfirmationEmail(recoveryRequest.getEmail(), confirmationCode);

            Code code = new Code();
            code.setCode(confirmationCode);
            code.setUser(user);
            code.setExpiryDate(LocalDateTime.now().plusDays(1));

            codeRepository.save(code);
        });
    }

    public void confirmRecoveryRequest(ConfirmRecovery confirmRecovery) {
        Optional<Code> codeOptional = codeRepository.findByCode(confirmRecovery.getCode());

        if (codeOptional.isPresent()) {
            Code code = codeOptional.get();
            if (code.getExpiryDate().isAfter(LocalDateTime.now())) {
                User user = code.getUser();
                String encodedPassword = passwordEncoder.encode(confirmRecovery.getNewPassword());
                user.setPassword(encodedPassword);
                userRepository.save(user);

                code.setUser(null);
                codeRepository.delete(code);
            } else {
                throw new CodeExpiredException("Code has expired");
            }
        } else {
            throw new InvalidCodeException("Invalid code");
        }
    }
}