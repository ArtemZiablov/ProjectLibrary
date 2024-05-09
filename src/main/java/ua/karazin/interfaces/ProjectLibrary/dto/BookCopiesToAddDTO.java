package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookCopiesToAddDTO(
        @NotNull
        Long isbn,

        @NotNull
        Integer copiesAmount
) {
}
