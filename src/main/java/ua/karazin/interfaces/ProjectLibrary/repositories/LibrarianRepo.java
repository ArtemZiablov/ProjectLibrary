package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;

import java.util.Optional;

@Repository
public interface LibrarianRepo extends JpaRepository<Librarian, Integer> {
    Optional<Librarian> findLibrarianById(int librarianId);
}