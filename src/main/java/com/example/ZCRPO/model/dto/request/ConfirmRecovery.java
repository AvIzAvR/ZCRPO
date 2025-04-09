package com.example.ZCRPO.model.dto.request;

public class ConfirmRecovery {
    private String code;
    private String newPassword;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}