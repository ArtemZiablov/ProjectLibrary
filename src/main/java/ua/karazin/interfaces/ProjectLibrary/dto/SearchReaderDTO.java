package ua.karazin.interfaces.ProjectLibrary.dto;

public record SearchReaderDTO(
        Integer readerId,
        String fullName,
        String phoneNumber
) {
}
