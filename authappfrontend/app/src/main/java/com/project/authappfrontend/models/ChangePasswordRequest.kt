package com.project.authappfrontend.models

data class ChangePasswordRequest(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)
