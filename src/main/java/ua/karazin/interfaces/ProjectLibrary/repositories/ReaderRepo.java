package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Optional;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Integer> {
    Optional<Reader> findById(Integer readerId);
}
