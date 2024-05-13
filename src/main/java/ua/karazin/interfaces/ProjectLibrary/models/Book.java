package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "Book")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString(exclude = {"bookReservations"})
public class Book {

    @Id
    @Column(name = "isbn")
    @NonNull
    private Long isbn;

    @Column(name = "title")
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title should be between 1 and 100 characters")
    @NonNull
    private String title;

    @Column(name = "year_of_publishing")
    @NotNull
    @Min(value = 1700, message = "Year of publishing should be greater than 1700")
    @Max(value = 2024, message = "Year of publishing should be less than 2024")
    @NonNull
    private Integer yearOfPublishing;

    @Column(name = "number_of_pages")
    @NotNull
    @Min(value = 2, message = "Number of pages should be greater than 2")
    @Max(value = 10000, message = "Number of pages should be less than 10 000")
    @NonNull
    private Integer numberOfPages;

    @Column(name = "annotation")
    @NotBlank(message = "Annotation is required")
    @Size(min = 10, max = 30000, message = "Annotation should be between 10 and 10000 characters")
    @NonNull
    private String annotation;

    @Column(name = "language")
    @NotBlank(message = "Language is required")
    @NonNull
    private String language;

    @Column(name = "book_photo")
    @NotBlank(message = "Book photo is required")
    @NonNull
    private String bookPhoto;

    @Column(name = "date_of_add")
    @Temporal(TemporalType.DATE)
    private Date dateOfAdd;

    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Author_of_the_book",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Translator_of_the_book",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "translator_id")
    )
    private List<Translator> translators;

    @NonNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Genre_of_the_book",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookReservation> bookReservations;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookCopy> bookCopies;


}
