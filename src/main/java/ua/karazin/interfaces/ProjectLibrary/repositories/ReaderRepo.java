package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Integer> {
    Optional<Reader> findById(Integer readerId);

    Optional<Reader> findByFullName(String name);

    @Query("SELECT r FROM Reader r WHERE LOWER(r.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    Optional<List<Reader>> findReadersByFullNameStartingWith(String fullName);

    Optional<List<Reader>> findReaderById(Integer id);

    Optional<List<Reader>> findReadersByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(r) FROM Reader r WHERE r.debtor = true ")
    int countDebtors();
}
