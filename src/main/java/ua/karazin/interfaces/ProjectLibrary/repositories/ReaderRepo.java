package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Integer> {
    Optional<Reader> findById(Integer readerId);

    Optional<Reader> findByFullName(String name);

    Optional<List<Reader>> findReadersByFullNameStartingWith(String fullName);

    Optional<List<Reader>> findReaderById(Integer id);

    Optional<List<Reader>> findReadersByPhoneNumber(String phoneNumber);
}
