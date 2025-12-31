package com.iit.formation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO {
    
    @NotBlank(message = "Le mot de passe actuel est requis")
    private String currentPassword;
    
    @NotBlank(message = "Le nouveau mot de passe est requis")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String newPassword;
    
    @NotBlank(message = "La confirmation du mot de passe est requise")
    private String confirmPassword;
    
    public ChangePasswordDTO() {}
    
    public ChangePasswordDTO(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    
    public String getCurrentPassword() {
        return currentPassword;
    }
    
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public boolean isPasswordMatch() {
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}

