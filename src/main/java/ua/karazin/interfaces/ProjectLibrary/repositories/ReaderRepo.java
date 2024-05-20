package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepo extends JpaRepository<Reader, Integer> {
    Optional<Reader> findById(Integer readerId);

    Optional<Reader> findByEmail(String email);

    Optional<Reader> findByFullName(String fullName);

    Optional<Reader> findByDateOfBirth(Date dateOfBirth);

    @Query("SELECT r.profilePhoto FROM Reader r WHERE r.id = :readerId")
    String findReadersPhoto(Integer readerId);

    @Query("SELECT r FROM Reader r WHERE LOWER(r.fullName) LIKE LOWER(CONCAT('%', :fullName, '%'))")
    Optional<List<Reader>> findReadersByFullNameStartingWith(String fullName);

    Optional<List<Reader>> findReaderById(Integer id);

    Optional<List<Reader>> findReadersByPhoneNumber(String phoneNumber);

    @Query("SELECT COUNT(r) FROM Reader r WHERE r.debtor = true ")
    int countDebtors();

    @Query("SELECT r FROM Reader r WHERE r.debtor = true ")
    List<Reader> findDebtors();

}
