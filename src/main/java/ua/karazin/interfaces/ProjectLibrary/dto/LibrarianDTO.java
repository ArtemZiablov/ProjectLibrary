package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LibrarianDTO(
        @NotBlank(message = "Full name is required")
        @Size(max = 100, message = "Full name must be less than 100 characters")
        String fullName,

        @NotBlank(message = "Password is required")
        @Size(max = 100, message = "Password must be less than 100 characters")
        String password,

        @NotBlank(message = "Phone number is required")
        @Size(max = 50, message = "Phone number must be less than 50 characters")
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Size(max = 50, message = "Email must be less than 50 characters")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Profile photo URL is required")
        @Size(max = 300, message = "Profile photo URL must be less than 300 characters")
        String profilePhoto
) {
}
