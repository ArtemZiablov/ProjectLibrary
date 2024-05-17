package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Book;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.models.BookOperation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookOperationRepo extends JpaRepository<BookOperation, Integer> {

    Optional<BookOperation> findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(BookCopy bookCopy, Reader reader);

    List<BookOperation> findBookOperationsByDateOfReturnIsNull();

    @Query("SELECT COUNT(bo) FROM BookOperation bo WHERE bo.dateOfReturn IS NULL AND bo.returnDeadline < CURRENT_DATE")
    int countOwedBooks();

    @Query("SELECT COUNT(DISTINCT bo.reader) FROM BookOperation bo WHERE bo.dateOfReturn IS NULL")
    int countOngoingReaders();

    @Query("SELECT DISTINCT bo.reader FROM BookOperation bo WHERE bo.dateOfReturn IS NULL")
    List<Reader> getOngoingReaders();

    @Query("SELECT bo.bookCopy.book FROM BookOperation bo WHERE bo.dateOfReturn IS NULL AND bo.reader.id = :readerId AND bo.bookCopy.book.isbn = :isbn")
    Optional<Book> findOpenBookOperationByReaderIdAndBookIsbn(Integer readerId, Long isbn);

}
