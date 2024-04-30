package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "admin")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int id;

    @Column(name = "full_name", nullable = false, length = 100)
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    @NonNull
    private String fullName;

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

    @Column(name = "profile_photo", nullable = false, length = 300)
    @NonNull
    private String profilePhoto;
}
