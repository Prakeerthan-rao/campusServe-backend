package com.campus.service.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    public static class Request {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Role is required: STUDENT, STAFF or ADMIN")
        private String role;

        public Request() {}

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }

    public static class Response {
        private String token;
        private String role;
        private Long   userId;
        private String username;
        private String fullName;
        private String email;

        public Response() {}

        public Response(String token, String role, Long userId,
                        String username, String fullName, String email) {
            this.token    = token;
            this.role     = role;
            this.userId   = userId;
            this.username = username;
            this.fullName = fullName;
            this.email    = email;
        }

        public String getToken()    { return token; }
        public void setToken(String token) { this.token = token; }

        public String getRole()     { return role; }
        public void setRole(String role) { this.role = role; }

        public Long getUserId()     { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail()    { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
