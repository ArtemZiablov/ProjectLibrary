package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Book_copy")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
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
}
