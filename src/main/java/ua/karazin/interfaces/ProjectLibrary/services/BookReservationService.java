package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.BookReservation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookReservationRepo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookReservationService {
    private final BookReservationRepo bookReservationRepo;
    private final BookProperties bookProperties;

    @Transactional
    public void addReservation(Book book, Reader reader, String status) {
        BookReservation bookReservation = new BookReservation(
                new Date(),
                reader,
                status,
                book
        );
        bookReservationRepo.save(bookReservation);
    }


    @Transactional
    public void deleteReservation(BookReservation bookReservation) {
        bookReservationRepo.delete(bookReservation);
    }

    public Optional<BookReservation> findBookReservationByBookAndReader(Book book, Reader reader) {
        return bookReservationRepo.findBookReservationByBookAndReader(book, reader);
    }

    public List<Reader> findReadersByBookReservationOrderByReservationDate(Long isbn) {
        return bookReservationRepo.findReadersByBookReservationOrderByReservationDate(isbn);
    }

    public List<Book> findReadersReservedBooks(Integer readerId) {
        return bookReservationRepo.findReadersReservedBooks(readerId);
    }

    public int countReadersWhoReservedBooks() {
        return bookReservationRepo.countReadersWhoReservedBooks();
    }

    public int countBookReservationsByIsbn(Long isbn) {
        return bookReservationRepo.countBookReservationsByIsbn(isbn);
    }

    public int countBookReservationsByReaderId(Integer readerId) {
        return bookReservationRepo.countBookReservationsByReaderId(readerId);
    }

    public int countAwaitReservationsByIsbn(Long isbn) {
        return bookReservationRepo.countAwaitReservationsByIsbn(isbn);
    }

    public BookReservation getOldestActiveReservationByIsbn(Long isbn) {
        return bookReservationRepo.getOldestActiveReservationByIsbn(isbn);
    }

    public List<BookReservation> findExpiredActiveReservations(int days) {
        Date cutoffDate = new Date(System.currentTimeMillis() - days * 24 * 60 * 60 * 1000);
        return bookReservationRepo.findExpiredActiveReservations(cutoffDate);
    }

    public void removeReservation(BookReservation reservation) {
        bookReservationRepo.delete(reservation);
    }

    public List<Reader> getReadersWhoReservedBooks() {
        return bookReservationRepo.getReadersWhoReservedBooks();
    }

    @Transactional
    public void activateReservation(BookReservation bookReservation) {
        bookReservation.setStatus(bookProperties.activeReservationStatus());
    }
}
