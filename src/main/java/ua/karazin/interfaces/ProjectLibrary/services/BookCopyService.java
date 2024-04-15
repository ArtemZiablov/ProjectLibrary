package ua.karazin.interfaces.ProjectLibrary.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopiesDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopyDTO;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookCopyNotExistException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookNotReturnedFromReaderException;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookCopyRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookCopyService {

    private final BookCopyRepo bookCopyRepo;
    private final BookOperationService bookOperationService;
    private final EntityManager entityManager;

    @Transactional
    public void addBookCopies(BookCopy bookCopy, int amount) {
        for (int i = 0; i < amount; i++) {
            BookCopy newBookCopy = new BookCopy();
            //newBookCopy.setIsbn(bookCopy.getIsbn());
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

}
