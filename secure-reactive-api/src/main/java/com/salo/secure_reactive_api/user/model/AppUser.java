package com.salo.secure_reactive_api.user.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public class AppUser {

    @Id
    private Long id;

    private String email;

    @Column("password_hash")
    private String passwordHash;

    // "ROLE_USER" o "ROLE_USER,ROLE_ADMIN"
    private String roles;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
}