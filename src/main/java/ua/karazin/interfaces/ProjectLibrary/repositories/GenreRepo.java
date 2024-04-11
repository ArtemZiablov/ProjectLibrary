package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;

import java.util.Optional;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Integer> {
    Optional<Genre> findByGenreName(String genreName);
}
