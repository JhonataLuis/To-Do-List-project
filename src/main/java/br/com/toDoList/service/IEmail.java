package br.com.toDoList.service;

public interface IEmail {

    public void sendWelcome(String email, String name);
    
    public void sendResetPassword(String email, String token);
}
