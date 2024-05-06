package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.BookReservation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookReservationRepo;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookReservationService {
    private final BookReservationRepo bookReservationRepo;

    @Transactional
    public void addReservation(Reader reader, Book book) {
        BookReservation bookReservation = new BookReservation(
                new Date(),
                reader,
                book
        );
        bookReservationRepo.save(bookReservation);
    }

    @Transactional
    public void deleteReservation(BookReservation bookReservation) {
        bookReservationRepo.delete(bookReservation);
    }

    public BookReservation findBookReservationByBookAndReader(Book book, Reader reader){
        return bookReservationRepo.findBookReservationByBookAndReader(book, reader);
    }

    public List<Reader> findReadersByBookReservationOrderByReservationDate(Integer isbn){
        return bookReservationRepo.findReadersByBookReservationOrderByReservationDate(isbn);
    }

    public int countReadersWhoReservedBooks(){
        return bookReservationRepo.countReadersWhoReservedBooks();
    }

    public int countBookReservationsByIsbn(Integer isbn){
        return bookReservationRepo.countBookReservationsByIsbn(isbn);
    }
}
