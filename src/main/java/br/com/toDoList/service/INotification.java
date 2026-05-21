package br.com.toDoList.service;

public interface INotification {

    void sendNotifications(String token, String title, String body, Long taskId);
}
