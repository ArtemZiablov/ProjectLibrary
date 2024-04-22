package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record BooksInfoDTO(
        @NotNull
        Integer isbn,

        @NotBlank(message = "Title is required")
        String title,

        @NotNull
        @Min(value = 1950, message = "Year of publishing should be greater than 1950")
        @Max(value = 2024, message = "Year of publishing should be less than 2024")
        Integer yearOfPublishing,

        @NotNull
        @Min(value = 2, message = "Number of pages should be greater than 2")
        @Max(value = 10000, message = "Number of pages should be less than 10 000")
        Integer numberOfPages,

        @NotBlank(message = "Annotation is required")
        @Size(min = 10, max = 1000, message = "Annotation should be between 10 and 1000 characters")
        String annotation,

        @NotBlank(message = "Language is required")
        String language,

        @NotBlank(message = "Book photo is required")
        String bookPhoto,

        List<AuthorDTO> authors,

        List<TranslatorDTO> translators,

        @NotNull
        List<GenreDTO> genres
) {
}
