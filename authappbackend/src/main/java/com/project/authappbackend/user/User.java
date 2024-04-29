package com.project.authappbackend.user;

import com.project.authappbackend.user.enums.Role;
import com.project.authappbackend.user.enums.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

//@Entity indicate this is a JPA entity
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    @Column(nullable = false)
    private @NotBlank String username;
    @Column(nullable = false, unique = true)
    private @NotBlank String email;
    @Column(nullable = false, unique = true)
    private @NotBlank String phoneNumber;
    @Column(nullable = false)
    private @NotBlank String address;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDateTime createdDateTime;
    @Column(nullable = false)
    private LocalDateTime updatedDateTime;
    @Column
    private LocalDateTime lastLoginDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(@NotBlank String name, @NotBlank String email, @NotBlank String phoneNumber, @NotBlank String address) {
        this.username = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(address, user.address);
    }

    @Override
    public int hashCode(){
        return Objects.hash(username, email, phoneNumber, address);
    }

    @Override
    public String toString(){
        return "User{" +
                ", name='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone number='" + phoneNumber + '\'' +
                ", address='" + address +
                '}';
    }

}
