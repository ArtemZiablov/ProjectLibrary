package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Reader")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString(exclude = {"bookCopies"})
public class Reader {
    @Id
    @Column(name = "reader_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @NotBlank(message = "Full name is required")
    @Size(min = 1, max = 100, message = "Full name should be between 1 and 100 characters")
    @NonNull
    private String fullName;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @NonNull
    private Date dateOfBirth;

    @Column(name = "password", nullable = false, length = 100)
    @NotBlank(message = "Password is required")
    @Size(max = 100, message = "Password must be less than 100 characters")
    @NonNull
    private String password;

    @Column(name = "phone_number", nullable = false, length = 50)
    @NotBlank(message = "Phone number is required")
    @Size(max = 50, message = "Phone number must be less than 50 characters")
    @NonNull
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 50)
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email must be less than 50 characters")
    @Email(message = "Invalid email format")
    @NonNull
    private String email;

    @Column(name = "profile_photo", length = 300)
    @NonNull
    private String profilePhoto;

    @Column(name = "debtor")
    private boolean debtor;

    @OneToMany(mappedBy = "reader")
    private List<BookCopy> bookCopies;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reader")
    private List<BookReservation> bookReservations;
}
