package com.bunon.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerRegistrationForm {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank @Email(message = "Enter a valid email")
    private String email;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String phone;
}
