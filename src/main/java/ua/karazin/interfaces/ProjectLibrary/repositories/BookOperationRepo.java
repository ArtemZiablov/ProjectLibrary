package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.models.BookOperation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookOperationRepo extends JpaRepository<BookOperation, Integer> {

    Optional<BookOperation> findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(BookCopy bookCopy, Reader reader);

    List<BookOperation> findBookOperationsByDateOfReturnIsNull();

    @Query("SELECT COUNT(b) FROM BookOperation b WHERE b.dateOfReturn IS NULL AND b.returnDeadline < CURRENT_DATE")
    int countOwedBooks();

    @Query("SELECT COUNT(DISTINCT b.reader) FROM BookOperation b WHERE b.dateOfReturn IS NULL")
    int countOngoingReaders();
}
