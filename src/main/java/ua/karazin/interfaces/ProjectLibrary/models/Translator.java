package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Translator")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString(exclude = {"books"})
public class Translator {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @NotBlank(message = "Full name is required")
    @Size(min = 1, max = 100, message = "Full name should be between 1 and 100 characters")
    @NonNull
    private String fullName;

    @ManyToMany(mappedBy = "translators")
    List<Book> books;
}
