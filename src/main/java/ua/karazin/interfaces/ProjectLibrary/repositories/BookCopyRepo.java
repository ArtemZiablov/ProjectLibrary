package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopyDTO;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCopyRepo extends JpaRepository<BookCopy, Integer> {
    Optional<BookCopy> findByCopyId(int copyId);

    @Query("SELECT new ua.karazin.interfaces.ProjectLibrary.dto.ReadersBookCopyDTO(" +
            "bc.book.isbn, bc.copyId, b.title, " +
            "(SELECT STRING_AGG(a.fullName, ', ') FROM Book b INNER JOIN b.authors a WHERE b.isbn = bc.book.isbn), " +
            "(SELECT STRING_AGG(g.genreName, ', ') FROM Book b INNER JOIN b.genres g WHERE b.isbn = bc.book.isbn), " +
            "ob.returnDeadline, bc.status, b.bookPhoto) " +
            "FROM BookCopy bc " +
            "INNER JOIN bc.book b " +
            "INNER JOIN BookOperation ob ON bc.copyId = ob.bookCopy.copyId " +
            "WHERE bc.reader.id = :readerId AND bc.status != 'free' AND ob.dateOfReturn IS NULL " +
            "GROUP BY bc.book.isbn, bc.copyId, b.title, ob.returnDeadline, bc.status, b.bookPhoto")
    List<ReadersBookCopyDTO> findReadersBookCopiesByReaderId(@Param("readerId") Integer readerId);

    @Query("SELECT bc FROM BookCopy bc WHERE bc.book.isbn = :isbn AND bc.status = :status")
    List<BookCopy> findBookCopiesByIsbnAndStatus(Integer isbn, String status);

    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.book.isbn = :isbn AND bc.status = 'free'")
    int countFreeBookCopiesByIsbn(@Param("isbn") Integer isbn);

    @Query("SELECT COUNT(bc) FROM BookCopy bc WHERE bc.status = 'taken'")
    int getAssignedBookCopiesCount();
}