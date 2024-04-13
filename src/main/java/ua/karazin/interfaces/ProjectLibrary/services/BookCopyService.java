package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookCopyNotFoundException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookNotReturnedFromReaderException;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookCopyRepo;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookNotRegisteredException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepo bookCopyRepo;

    @Transactional
    public void addBookCopies(BookCopy bookCopy, int amount) {
        for (int i = 0; i < amount; i++) {
            BookCopy newBookCopy = new BookCopy();
            newBookCopy.setIsbn(bookCopy.getIsbn());
            newBookCopy.setStatus(bookCopy.getStatus());

            bookCopyRepo.save(newBookCopy);
        }
    }

    Optional<BookCopy> findBookCopyByCopyId(int copyId){
        return bookCopyRepo.findByCopyId(copyId);
    }

    @Transactional
    public void deleteBookCopy(int copyId) {
        var bookCopyToDelete = findBookCopyByCopyId(copyId);
        if (bookCopyToDelete.isEmpty()){
            throw new BookCopyNotFoundException();
        } else if (bookCopyToDelete.get().getReader().isPresent()){
            throw new BookNotReturnedFromReaderException();
        } else {
            bookCopyRepo.delete(bookCopyToDelete.get());
        }
    }
}
