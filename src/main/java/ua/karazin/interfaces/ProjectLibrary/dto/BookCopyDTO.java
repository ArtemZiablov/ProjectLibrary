package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookCopyDTO(
        @NotNull
        Integer isbn,

        ReaderDTO reader // ?????????????
) {
}
