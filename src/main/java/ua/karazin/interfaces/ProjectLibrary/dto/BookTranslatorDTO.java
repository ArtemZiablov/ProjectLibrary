package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookTranslatorDTO(
        @NotNull
        BookToAddDTO book,

        @NotNull
        TranslatorDTO translator
) {
}
