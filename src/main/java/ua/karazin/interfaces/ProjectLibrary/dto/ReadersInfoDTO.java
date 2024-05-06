package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Date;

public record ReadersInfoDTO(
        Integer readerId,
        String fullName,
        Date dateOfBirth,
        String phoneNumber,
        String email,
        Boolean debtor,
        String profilePhoto,
        ReadersBookCopiesDTO readersBooks
) {
}
