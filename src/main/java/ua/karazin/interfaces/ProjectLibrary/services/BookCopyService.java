package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopiesDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopyDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookCopyNotExistException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookIsReservedException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookNotReturnedFromReaderException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.OpenBookOperationAlreadyExists;
import ua.karazin.interfaces.ProjectLibrary.models.*;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookCopyRepo;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j(topic = "BookCopyService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookCopyService {

    private final BookCopyRepo bookCopyRepo;
    private final BookOperationService bookOperationService;
    private final BookReservationService bookReservationService;

    @Transactional
    public void addBookCopies(BookCopy bookCopy, int amount) {
        for (int i = 0; i < amount; i++) {
            BookCopy newBookCopy = new BookCopy();
            newBookCopy.setBook(bookCopy.getBook());
            newBookCopy.setStatus(bookCopy.getStatus());

            bookCopyRepo.save(newBookCopy);
        }
    }

    public Optional<BookCopy> findBookCopyByCopyId(int copyId){
        return bookCopyRepo.findByCopyId(copyId);
    }

    @Transactional
    public void deleteBookCopy(int copyId) {
        var bookCopyToDelete = findBookCopyByCopyId(copyId);
        if (bookCopyToDelete.isEmpty()){
            throw new BookCopyNotExistException();
        } else if (bookCopyToDelete.get().getReader().isPresent()){
            throw new BookNotReturnedFromReaderException();  // TODO додати перевірку, чи книга заброньована, тоді перекинути бронювання на інший екземпляр
        } else {
            bookCopyRepo.delete(bookCopyToDelete.get());
        }
    }

    @Transactional
    public void assignBookCopy(BookCopy bookCopy, Reader reader, Librarian librarian) {
        var book = bookCopy.getBook();
        int freeBookCopiesCount = countFreeBookCopiesByIsbn(book.getIsbn());
        List<Reader> readersWhoReservedBook = bookReservationService.
                findReadersByBookReservationOrderByReservationDate(book.getIsbn());
        if (bookOperationService.findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(bookCopy, reader).isPresent())
            throw new OpenBookOperationAlreadyExists();

        if (readersWhoReservedBook.isEmpty() || (freeBookCopiesCount > readersWhoReservedBook.size() && !readersWhoReservedBook.contains(reader))){
            log.debug("!readersWhoReservedBook.contains(reader) {}", !readersWhoReservedBook.contains(reader));
            proceedAssigning(bookCopy, reader, librarian);
        } else if(readersWhoReservedBook.contains(reader) && readersWhoReservedBook.indexOf(reader) + 1 <= freeBookCopiesCount){
            log.info("reader: {}", reader.getId());
            log.info("book: {}", book.getIsbn());
            log.info("freeBookCopiesCount: {}", freeBookCopiesCount);

            bookReservationService.deleteReservation(bookReservationService.findBookReservationByBookAndReader(book, reader));
            proceedAssigning(bookCopy, reader, librarian);
        } else
            throw new BookIsReservedException();
    }


    protected void proceedAssigning(BookCopy bookCopy, Reader reader, Librarian librarian){
        bookOperationService.addOperation(bookCopy, reader, librarian);
        bookCopy.setReader(reader);
        bookCopy.setStatus("taken");
        reader.getBookCopies().add(bookCopy);
    }

    @Transactional
    public void releaseBookCopy(BookCopy bookCopy, Reader reader) {
        bookOperationService.releaseOperation(bookCopy, reader);
        bookCopy.setStatus("free");
        bookCopy.setReader(null);
    }

    public ReadersBookCopiesDTO getReadersBookCopies(Integer readerId) {
        List<ReadersBookCopyDTO> readersBooks = bookCopyRepo.findReadersBookCopiesByReaderId(readerId);
        return new ReadersBookCopiesDTO(readersBooks);
    }

    public int countFreeBookCopiesByIsbn(Integer isbn) {
        return bookCopyRepo.countFreeBookCopiesByIsbn(isbn);
    }

    public Long countAllBookCopies(){
        return bookCopyRepo.count();
    }

    public Integer countAssignedBookCopies(){
        return bookCopyRepo.getAssignedBookCopiesCount();
    }
}
