package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;


@Entity
@Table(name = "Book_copy")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copyId")
    private int copyId;

    @Column(name = "isbn", nullable = false)
    @NonNull
    private Integer isbn;


    @Column(name = "status", nullable = false)
    @NonNull
    private String status;

    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "reader_id")
    private Reader reader;

    public Optional<Reader> getReader() {
        return Optional.ofNullable(reader);
    }
}
