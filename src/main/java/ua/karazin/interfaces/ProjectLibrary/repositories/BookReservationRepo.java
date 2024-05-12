package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.BookReservation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookReservationRepo extends JpaRepository<BookReservation, Integer> {

    Optional<BookReservation> findBookReservationByBookAndReader(Book book, Reader reader);

    @Query("SELECT r FROM Reader r " +
            "JOIN BookReservation br ON r.id = br.reader.id " +
            "WHERE br.book.isbn = :isbn " +
            "ORDER BY br.dateOfReservation ASC")
    List<Reader> findReadersByBookReservationOrderByReservationDate(Long isbn);

    @Query("SELECT COUNT(DISTINCT br.reader) FROM BookReservation br")
    int countReadersWhoReservedBooks();

    @Query("SELECT COUNT(br) FROM BookReservation br WHERE br.book.isbn = :isbn")
    int countBookReservationsByIsbn(Long isbn);

    @Query("SELECT COUNT(br) FROM BookReservation br WHERE br.reader.id = :readerId")
    int countBookReservationsByReaderId(Integer readerId);

    @Query("SELECT br.book FROM BookReservation br WHERE br.reader.id = :readerId")
    List<Book> findReadersReservedBooks(Integer readerId);

    List<BookReservation> findByDateOfReservationBefore(Date date);
}
