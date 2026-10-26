package com.project.employeService.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EmployeeRequestDTO {

    @NotBlank(message = "Name is Required")
    @Size(min=3,max=100)
    @Pattern(regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$", message = "Name must contain only letters and single space between words")
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

    public EmployeeRequestDTO() {
    }

    public void setName(@NotBlank(message = "Name is Required") @Size(min = 3, max = 100) String name) {
        this.name = name;
    }

    public void setEmail(@NotBlank(message = "Email is Required") @Email String email) {
        this.email = email;
    }

    public void setPassword(@NotBlank(message = "Password is Required") @Size(min = 7, max = 72) String password) {
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
                '}';
    }
}
