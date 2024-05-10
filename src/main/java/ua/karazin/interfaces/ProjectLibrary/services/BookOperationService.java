package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookOperationDoesntExistException;
import ua.karazin.interfaces.ProjectLibrary.models.*;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookOperationRepo;

import java.util.*;

@Slf4j(topic = "BookOperationService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookOperationService {
    private final BookOperationRepo bookOperationRepo;


    @Transactional
    public void addOperation(BookCopy bookCopy, Reader reader, Librarian librarian) {
        BookOperation bookOperation = new BookOperation();
        // Отримуємо поточну дату
        Date currentDate = new Date();

        bookOperation.setDateOfIssuance(currentDate);
        log.debug(bookOperation.getDateOfIssuance().toString());

        // Додаємо 30 днів до поточної дати
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        // Отримуємо дату через 30 днів
        Date returnDeadline = calendar.getTime();
        // Встановлюємо цю дату у поле returnDeadline об'єкта bookOperation
        bookOperation.setReturnDeadline(returnDeadline);
        log.debug(bookOperation.getReturnDeadline().toString());

        bookOperation.setLibrarian(librarian);

        bookOperation.setReader(reader);

        bookOperation.setBookCopy(bookCopy);

        bookOperationRepo.save(bookOperation);
    }

    public Optional<BookOperation> findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(BookCopy bookCopy, Reader reader){
        return bookOperationRepo.findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(bookCopy, reader);
    }

    @Transactional
    public void releaseOperation(BookCopy bookCopy, Reader reader){
        var operation = findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(bookCopy, reader);
        if (operation.isEmpty())
            throw new BookOperationDoesntExistException();
        operation.get().setDateOfReturn(new Date());
    }

    public List<BookOperation> findBookOperationsByDateOfReturnIsNull(){
        return bookOperationRepo.findBookOperationsByDateOfReturnIsNull();
    }

    public Optional<Book> findOpenBookOperationByReaderIdAndBookIsbn(Integer readerId, Long isbn){
        return bookOperationRepo.findOpenBookOperationByReaderIdAndBookIsbn(readerId, isbn);
    }

    public int countOwedBooks(){
        return bookOperationRepo.countOwedBooks();
    }

    public int countOngoingReaders(){
        return bookOperationRepo.countOngoingReaders();
    }
}
