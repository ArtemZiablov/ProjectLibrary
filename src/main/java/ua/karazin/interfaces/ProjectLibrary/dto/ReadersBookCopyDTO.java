package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Date;

public record ReadersBookCopyDTO(
        Long isbn,
        Integer copyId,
        String title,
        Object authors,
        Object genres,
        Date returnDeadline,
        String status,
        String bookPhoto
) {
}
