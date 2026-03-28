package br.com.toDoList.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetConfirm {

    @NotBlank(message = "O token é obrigatório")
    private String token;

    @NotBlank(message = "A senha é obrigatório")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
