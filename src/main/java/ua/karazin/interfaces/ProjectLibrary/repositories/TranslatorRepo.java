package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Translator;

import java.util.Optional;

@Repository
public interface TranslatorRepo extends JpaRepository<Translator, Integer> {
    Optional<Translator> findByFullName(String name);
}
