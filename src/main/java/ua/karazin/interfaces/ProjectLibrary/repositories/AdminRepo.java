package ua.karazin.interfaces.ProjectLibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.karazin.interfaces.ProjectLibrary.models.Admin;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByFullName(String name);

}
