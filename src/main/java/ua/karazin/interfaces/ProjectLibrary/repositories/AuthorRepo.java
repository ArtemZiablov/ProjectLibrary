package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Author;

import java.util.Date;
import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer> {
    Optional<Author> findByFullNameAndDateOfBirth(String name, Date dateOfBirth);

}
