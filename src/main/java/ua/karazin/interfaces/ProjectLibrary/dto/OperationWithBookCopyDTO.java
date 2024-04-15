package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

public record OperationWithBookCopyDTO(
        @NotNull
        Integer readerId,

        @NotNull
        Integer bookCopyId
) {
}
