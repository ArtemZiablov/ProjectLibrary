package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record BookDTO(
        @NotNull
        Long isbn,

        @NotBlank(message = "Title is required")
        String title,

        @NotNull
        @Min(value = 1700, message = "Year of publishing should be greater than 1700")
        @Max(value = 2024, message = "Year of publishing should be less than 2024")
        Integer yearOfPublishing,

        @NotNull
        @Min(value = 2, message = "Number of pages should be greater than 2")
        @Max(value = 10000, message = "Number of pages should be less than 10 000")
        Integer numberOfPages,

        @NotBlank(message = "Annotation is required")
        @Size(min = 10, max = 30000, message = "Annotation should be between 10 and 30000 characters")
        String annotation,

        @NotBlank(message = "Language is required")
        String language,

        @NotBlank(message = "Book photo is required")
        String bookPhoto,

        @NotNull
        Integer copiesAmount,

        // not null, because folk books might appear
        List<AuthorDTO> authors,

        // not null, because it can be an original text
        List<TranslatorDTO> translators,

        @NotNull
        List<GenreDTO> genres

        ) {
}
