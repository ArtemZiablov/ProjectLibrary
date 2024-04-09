package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Operations_with_books")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString
public class BookOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_issuance")
    @Temporal(TemporalType.DATE)
    private Date dateOfIssuance;

    @Column(name = "return_deadline")
    @Temporal(TemporalType.DATE)
    private Date returnDeadline;

    @Column(name = "date_of_return")
    @Temporal(TemporalType.DATE)
    private Date dateOfReturn;

    @ManyToOne
    @JoinColumn(name = "id")
    @NonNull
    private Librarian librarian;

    @ManyToOne
    @JoinColumn(name = "id")
    @NonNull
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "isbn")
    @NonNull
    private Book book;
}
