package com.bunon.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class WeaverRegistrationForm {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank @Email(message = "Enter a valid email")
    private String email;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Location (district) is required")
    private String location;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    // TODO (F16 owner): stricter NID validation (10 / 13 / 17 digits)
    @NotBlank(message = "NID number is required")
    private String nidNumber;

    private MultipartFile nidImage;
}
