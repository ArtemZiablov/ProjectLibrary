package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Date;

public record ReadersBookCopyDTO(
        Integer isbn,
        Integer copyId,
        String title,
        Object authors,
        Object genres,
        Date returnDeadline,
        String status,
        String bookPhoto
) {
}
