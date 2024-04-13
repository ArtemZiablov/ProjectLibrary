package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record BookCopyToDeleteDTO(

        @NotNull
        Integer copyId
) {
}
