package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OperationWithBookCopyDTO(
        @NotNull
        Integer readerId,

        @NotNull
        List<Integer> bookCopiesId
) {
}
