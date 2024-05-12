package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;

import java.util.Optional;

@Repository
public interface LibrarianRepo extends JpaRepository<Librarian, Integer> {
    Optional<Librarian> findLibrarianById(int librarianId);

    Optional<Librarian> findByFullName(String name);

    Optional<Librarian> findByEmail(String email);

    @Query("SELECT l.profilePhoto FROM Librarian l WHERE l.id = :librarianId")
    String findLibrariansPhoto(Integer librarianId);
}
