package ua.karazin.interfaces.ProjectLibrary.dto;

public record RegisterLibrarianDTO(
        String fullName,
        String email,
        String phoneNumber,
        String password,
        String photo
) {
}
