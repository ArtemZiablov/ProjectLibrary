package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Date;

public record BookOperationDTO(
        @NotNull
        Date dateOfIssuance,

        @NotNull
        @Temporal(TemporalType.DATE)
        Date returnDeadline,

        @Temporal(TemporalType.DATE)
        Date dateOfReturn,

        @NotNull
        LibrarianDTO librarian,

        @NotNull
        ReaderDTO reader,

        @NotNull
        BookDTO book
) {
}
