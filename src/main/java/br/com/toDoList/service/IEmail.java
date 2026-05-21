package br.com.toDoList.service;

public interface IEmail {

    void sendWelcome(String email, String name);

    void sendResetPassword(String email, String token);
}
