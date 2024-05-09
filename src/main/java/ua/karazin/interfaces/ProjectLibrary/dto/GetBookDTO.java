package ua.karazin.interfaces.ProjectLibrary.dto;

public record GetBookDTO(
        Long isbn,
        String title,
        String authors,
        String genres,
        String bookPhoto
) {
}
