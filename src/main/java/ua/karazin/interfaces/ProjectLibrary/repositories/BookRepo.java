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
public interface BookRepo extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(Long isbn);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findBooksByTitleStartingWith(String title);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.fullName) LIKE LOWER(CONCAT('%', :authorsName, '%'))")
    List<Book> findBooksByAuthorsStartingWith(String authorsName);

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE LOWER(g.genreName) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Book> findBooksByGenreStartingWith(String genre);

    @Query("SELECT b FROM Book b ORDER BY b.dateOfAdd DESC")
    Page<Book> findNovelties(Pageable pageable);

}
