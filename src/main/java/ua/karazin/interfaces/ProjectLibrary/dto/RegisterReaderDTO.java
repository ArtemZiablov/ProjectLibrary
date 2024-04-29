package ua.karazin.interfaces.ProjectLibrary.dto;

import java.util.Date;

public record RegisterReaderDTO(
        String fullName,
        Date dateOfBirth,
        String email,
        String phoneNumber,
        String password,
        String photo
        ) {
}
