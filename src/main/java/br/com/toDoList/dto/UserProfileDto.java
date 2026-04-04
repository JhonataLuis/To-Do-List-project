package br.com.toDoList.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileDto {

    private String name;
    private MultipartFile profilePhoto;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public MultipartFile getProfilePhoto() {
        return profilePhoto;
    }
    public void setProfilePhoto(MultipartFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    
}
