package ua.karazin.interfaces.ProjectLibrary.dto;

public record LibrariansInfoDTO(
        Integer librarianId,
        String fullName,
        String phoneNumber,
        String email,
        String profilePhoto
) {
}
