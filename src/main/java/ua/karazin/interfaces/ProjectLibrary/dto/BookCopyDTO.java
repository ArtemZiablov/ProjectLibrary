package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

public record BookCopyDTO(
        @NotNull
        Integer isbn,

        @NotNull
        ReaderDTO reader ///////////////////////////////////////////
) {
}
