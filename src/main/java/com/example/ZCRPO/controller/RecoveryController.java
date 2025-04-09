package com.example.ZCRPO.controller;

import com.example.ZCRPO.model.dto.request.ConfirmRecovery;
import com.example.ZCRPO.model.dto.request.RecoveryRequest;
import com.example.ZCRPO.service.impl.UserRecoveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recovery")
public class RecoveryController {

    private final UserRecoveryService userRecoveryService;

    public RecoveryController(UserRecoveryService userRecoveryService) {
        this.userRecoveryService = userRecoveryService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestRecovery(@RequestBody RecoveryRequest recoveryRequest) {
        userRecoveryService.recoveryRequest(recoveryRequest);
        return ResponseEntity.ok("Recovery request sent successfully");
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmRecovery(@RequestBody ConfirmRecovery confirmRecovery) {

        try {
            userRecoveryService.confirmRecoveryRequest(confirmRecovery);
            return ResponseEntity.ok("Password updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}