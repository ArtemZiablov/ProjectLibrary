package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookGenreDTO(
        @NotNull
        BookDTO book,

        @NotNull
        GenreDTO genre
) {
}
