package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Author")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString(exclude = {"books"})
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @NotBlank(message = "Full name is required")
    @Size(min = 1, max = 100, message = "Full name should be between 1 and 100 characters")
    @NonNull
    private String fullName;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @NonNull
    private Date dateOfBirth;

    @Column(name = "nationality")
    @NotBlank(message = "Nationality is required")
    @Size(min = 1, max = 50, message = "Nationality should be between 1 and 50 characters")
    @NonNull
    private String nationality;

    @ManyToMany(mappedBy = "authors")
    private List<Book> books;
}
