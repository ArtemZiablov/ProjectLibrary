package ua.karazin.interfaces.ProjectLibrary.dto;

public record SearchBookDTO(
        Integer isbn,
        String title,
        String authors,
        String genres,
        String bookPhoto
) {
}
