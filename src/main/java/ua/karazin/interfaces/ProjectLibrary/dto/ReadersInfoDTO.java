package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Date;
import java.util.List;

public record ReadersInfoDTO(
        Integer readerId,
        String fullName,
        Date dateOfBirth,
        String phoneNumber,
        String email,
        Boolean debtor,
        String profilePhoto,
        List<ReadersBookCopyDTO> readersBooks,
        List<GetReservedBookDTO> reservedBooks,
        List<ReadersReturnedBookDTO> history

) {
}
