package com.project.employeService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmployeeRequestDTO {

    @NotBlank(message = "Name is Required")
    @Size(min=3,max=100)
    private String name;
    @NotBlank(message = "Email is Required")
    @Email
    private String email;
    @NotBlank(message = "Password is Required")
    @Size(min=7,max=72)
    private String password;



    public EmployeeRequestDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "EmployeeRequestDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
