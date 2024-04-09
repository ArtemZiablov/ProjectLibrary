package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Date;

public record BookReservationDTO(
        @NotNull
        @Temporal(TemporalType.TIMESTAMP)
        Date dateOfReservation,

        @NotNull
        ReaderDTO reader,

        @NotNull
        BookDTO book
) {
}
