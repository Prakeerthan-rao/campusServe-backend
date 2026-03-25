package com.campus.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StaffRegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    @NotBlank
    private String fullName;

    private Long departmentId;

    public String getUsername()         { return username; }
    public void setUsername(String u)   { this.username = u; }

    public String getPassword()         { return password; }
    public void setPassword(String p)   { this.password = p; }

    public String getEmail()            { return email; }
    public void setEmail(String e)      { this.email = e; }

    public String getFullName()         { return fullName; }
    public void setFullName(String f)   { this.fullName = f; }

    public Long getDepartmentId()       { return departmentId; }
    public void setDepartmentId(Long d) { this.departmentId = d; }
}