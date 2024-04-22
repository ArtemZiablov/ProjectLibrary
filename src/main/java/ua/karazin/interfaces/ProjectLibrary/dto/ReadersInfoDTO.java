package ua.karazin.interfaces.ProjectLibrary.dto;

public record ReadersInfoDTO(
        Integer readerId,
        String fullName,
        String phoneNumber,
        String email,
        ReadersBookCopiesDTO readersBooks
) {
}
