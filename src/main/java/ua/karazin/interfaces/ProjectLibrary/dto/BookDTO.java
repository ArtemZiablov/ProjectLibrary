package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.*;
import lombok.NonNull;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.models.Translator;

import java.util.List;

public record BookDTO(
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

        @NonNull
        @ManyToMany
        @JoinTable(
                name = "Author_of_the_book",
                joinColumns = @JoinColumn(name = "isbn"),
                inverseJoinColumns = @JoinColumn(name = "id")
        )
        List<AuthorDTO> authors,  /////////////////////////////////////

        @NonNull
        @ManyToMany
        @JoinTable(
                name = "Translator_of_the_book",
                joinColumns = @JoinColumn(name = "isbn"),
                inverseJoinColumns = @JoinColumn(name = "id")
        )
        List<TranslatorDTO> translators//////////////////////////////////////////////////////////////////
        ) {
}
