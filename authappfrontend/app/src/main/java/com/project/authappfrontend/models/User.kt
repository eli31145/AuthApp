package com.project.authappfrontend.models

import java.time.LocalDateTime

data class User(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val address: String,
    val password: String?,
    val createdDateTime: LocalDateTime?,
    val updatedDateTime: LocalDateTime?,
    val lastLogin: LocalDateTime?,
    val userStatus: String?,
    val role: String?
)
