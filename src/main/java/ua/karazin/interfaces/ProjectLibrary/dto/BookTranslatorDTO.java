package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookTranslatorDTO(
        @NotNull
        BookDTO book,

        @NotNull
        TranslatorDTO translator
) {
}
