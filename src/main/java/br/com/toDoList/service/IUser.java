package br.com.toDoList.service;

import org.springframework.web.multipart.MultipartFile;

import br.com.toDoList.dto.ChangePasswordRequest;
import br.com.toDoList.model.User;

public interface IUser {

    User getCurrentUser();

    User updateProfile(Long id, String name);

    String uploadPhoto(Long id, MultipartFile file);

    void changePassword(ChangePasswordRequest request);


}
