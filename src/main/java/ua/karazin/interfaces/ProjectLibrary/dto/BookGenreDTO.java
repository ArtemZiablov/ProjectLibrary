package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;
import ua.karazin.interfaces.ProjectLibrary.models.Book;

public record BookGenreDTO(
        @NotNull
        BookDTO book,

        @NotNull
        GenreDTO genre
) {
}
