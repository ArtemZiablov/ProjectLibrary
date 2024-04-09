package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreDTO(
        @NotBlank(message = "Genre name is required")
        @Size(min = 1, max = 100, message = "Genre name should be between 1 and 100 characters")
        String genreName
) {
}
