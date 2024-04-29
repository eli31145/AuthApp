package com.project.authappbackend.user.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChangePasswordRequest {
    private String user;
    private String oldPassword;
    private String newPassword;
}
