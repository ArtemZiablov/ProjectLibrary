package ua.karazin.interfaces.ProjectLibrary.dto;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import ua.karazin.interfaces.ProjectLibrary.models.Book;

import java.util.Date;
import java.util.List;

public record AuthorDTO(
        @NotBlank(message = "Full name is required")
        @Size(min = 1, max = 100, message = "Full name should be between 1 and 100 characters")
        @NonNull
        String fullName,

        @Temporal(TemporalType.DATE)
        @NonNull
        Date dateOfBirth,

        @NotBlank(message = "Nationality is required")
        @Size(min = 1, max = 50, message = "Nationality should be between 1 and 50 characters")
        @NonNull
        String nationality
) {
}
