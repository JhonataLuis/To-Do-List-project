package br.com.toDoList.service;

import br.com.toDoList.dto.AuthResponse;
import br.com.toDoList.dto.LoginRequest;
import br.com.toDoList.dto.RegisterRequest;

public interface IAuth {

    AuthResponse login(LoginRequest request);

    void register(RegisterRequest request);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);
}
