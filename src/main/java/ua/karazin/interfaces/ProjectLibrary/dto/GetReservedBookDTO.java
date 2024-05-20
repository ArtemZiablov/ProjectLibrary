package ua.karazin.interfaces.ProjectLibrary.dto;

public record GetReservedBookDTO(
        Long isbn,
        String title,
        String authors,
        String genres,
        String bookPhoto,
        String daysLeft
) {
}
