package com.gerardo.churchmanager.backend.user.entity;

import com.gerardo.churchmanager.backend.user.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")

@Getter
@Setter
@Builder

@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
