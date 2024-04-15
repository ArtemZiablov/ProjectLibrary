package ua.karazin.interfaces.ProjectLibrary.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "Book")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
@ToString
public class Book {

    @Id
    @Column(name = "isbn")
    @NonNull
    private Integer isbn;

    @Column(name = "title")
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title should be between 1 and 100 characters")
    @NonNull
    private String title;

    @Column(name = "year_of_publishing")
    @NotNull
    @Min(value = 1950, message = "Year of publishing should be greater than 1950")
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
    @Size(min = 10, max = 1000, message = "Annotation should be between 10 and 1000 characters")
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

    @NonNull
    @ManyToMany
    @JoinTable(
            name = "Author_of_the_book",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @NonNull
    @ManyToMany
    @JoinTable(
            name = "Translator_of_the_book",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "translator_id")
    )
    private List<Translator> translators;

    @NonNull
    @ManyToMany
    @JoinTable(
            name = "Genre_of_the_book",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @OneToMany(mappedBy = "book")
    private List<BookReservation> bookReservations;

    @OneToMany(mappedBy = "book")
    private List<BookCopy> bookCopies;


}
