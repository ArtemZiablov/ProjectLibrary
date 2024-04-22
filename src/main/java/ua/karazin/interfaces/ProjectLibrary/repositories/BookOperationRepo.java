package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.models.BookOperation;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Optional;

@Repository
public interface BookOperationRepo extends JpaRepository<BookOperation, Integer> {

    Optional<BookOperation> findBookOperationByBookCopyAndReaderAndDateOfReturnIsNull(BookCopy bookCopy, Reader reader);
}
