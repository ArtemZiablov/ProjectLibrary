package ua.karazin.interfaces.ProjectLibrary.dto;

public record GetBookDTO(
        Integer isbn,
        String title,
        String authors,
        String genres,
        String bookPhoto
) {
}
