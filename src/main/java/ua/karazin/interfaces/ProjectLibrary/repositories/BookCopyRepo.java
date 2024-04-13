package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;

import java.util.Optional;

@Repository
public interface BookCopyRepo extends JpaRepository<BookCopy, Integer> {
    Optional<BookCopy> findByCopyId(int copyId);
}