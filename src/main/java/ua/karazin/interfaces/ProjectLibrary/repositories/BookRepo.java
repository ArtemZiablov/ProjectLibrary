package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    Optional<Book> findByIsbn(Integer isbn);

    List<Book> findBooksByTitleStartingWith(String titlePrefix);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.fullName LIKE CONCAT(:authorsPrefix, '%')")
    List<Book> findBooksByAuthorsStartingWith(String authorsPrefix);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.genreName LIKE CONCAT(:genrePrefix, '%')")
    List<Book> findBooksByGenreStartingWith(String genrePrefix);

    @Query("SELECT b FROM Book b ORDER BY b.dateOfAdd DESC")
    Page<Book> findNovelties(Pageable pageable);
}
