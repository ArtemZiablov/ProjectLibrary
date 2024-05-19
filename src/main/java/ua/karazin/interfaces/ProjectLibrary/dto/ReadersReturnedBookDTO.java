package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Date;

public record ReadersReturnedBookDTO(
        Long isbn,
        String title,
        String authors,
        String genres,
        String bookPhoto,
        Date assignDate,
        Date releaseDate
) {
}
