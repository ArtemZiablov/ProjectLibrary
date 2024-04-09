package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "Book_reservation")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString
public class BookReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_reservation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfReservation;

    @ManyToOne
    @JoinColumn(name = "id")
    @NonNull
    private Reader reader;

    @ManyToOne
    @JoinColumn(name = "isbn")
    @NonNull
    private Book book;
}