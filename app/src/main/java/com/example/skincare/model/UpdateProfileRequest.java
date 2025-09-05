package com.example.skincare.model;

public class UpdateProfileRequest {

    private String username;
    private String email;
    private Integer age;
    private String gender;
    private String password;                  // optional
    private String password_confirmation;     // required if password is set

    // Constructor
    public UpdateProfileRequest(String username, String email, Integer age, String gender, String password, String password_confirmation) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPassword_confirmation() { return password_confirmation; }
    public void setPassword_confirmation(String password_confirmation) { this.password_confirmation = password_confirmation; }
}
