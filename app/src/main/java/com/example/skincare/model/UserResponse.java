package com.example.skincare.model;

public class UserResponse {

    private String status;
    private String message;
    private User user;
    private String token; // optional if your API returns it

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Inner User class
    public static class User {
        private int id;
        private String username;
        private String email;
        private Integer age;
        private String gender;
        private String email_verified_at;
        private String created_at;
        private String updated_at;

        // Getters and setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getEmail_verified_at() { return email_verified_at; }
        public void setEmail_verified_at(String email_verified_at) { this.email_verified_at = email_verified_at; }

        public String getCreated_at() { return created_at; }
        public void setCreated_at(String created_at) { this.created_at = created_at; }

        public String getUpdated_at() { return updated_at; }
        public void setUpdated_at(String updated_at) { this.updated_at = updated_at; }
    }
}
