package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TranslatorDTO(
        @NotBlank(message = "Full name is required")
        @Size(min = 1, max = 100, message = "Full name should be between 1 and 100 characters")
        String translator
) {
}
