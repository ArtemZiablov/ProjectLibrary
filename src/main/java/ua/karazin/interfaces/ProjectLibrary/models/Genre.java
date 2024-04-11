package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Genre")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private int id;

    @Column(name = "genre_name")
    @NotBlank(message = "Genre name is required")
    @Size(min = 1, max = 100, message = "Genre name should be between 1 and 100 characters")
    @NonNull
    private String genreName;

    @ManyToMany(mappedBy = "genres")
    private List<Book> books;
}
