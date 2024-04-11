package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookGenreDTO(
        @NotNull
        BookToAddDTO book,

        @NotNull
        GenreDTO genre
) {
}
