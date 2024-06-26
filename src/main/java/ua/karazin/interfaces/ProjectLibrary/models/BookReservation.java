package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Book_reservation")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class BookReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int id;

    @Column(name = "date_of_reservation")
    @Temporal(TemporalType.TIMESTAMP)
    @NonNull
    private Date dateOfReservation;

    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "reader_id")
    @NonNull
    private Reader reader;

    @Column(name = "status")
    @NonNull
    private String status;

    @ManyToOne
    @JoinColumn(name = "book_isbn", referencedColumnName = "isbn")
    @NonNull
    private Book book;
}