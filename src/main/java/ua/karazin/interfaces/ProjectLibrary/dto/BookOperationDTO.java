package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


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
