package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Book;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(Integer isbn);
}
