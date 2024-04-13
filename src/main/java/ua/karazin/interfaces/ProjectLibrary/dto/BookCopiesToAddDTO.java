package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookCopiesToAddDTO(
        @NotNull
        Integer isbn,

        @NotNull
        Integer copiesAmount
) {
}
