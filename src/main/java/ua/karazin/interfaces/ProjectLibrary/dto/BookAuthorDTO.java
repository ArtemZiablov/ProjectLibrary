package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookAuthorDTO(
        @NotNull
        BookToAddDTO book,

        @NotNull
        AuthorDTO author
) {
}
