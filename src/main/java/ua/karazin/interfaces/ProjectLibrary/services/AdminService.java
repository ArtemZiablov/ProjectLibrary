package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Admin;
import ua.karazin.interfaces.ProjectLibrary.repositories.AdminRepo;

import java.util.Optional;

@Slf4j(topic = "AdminService")
@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepo adminRepo;

    public void save(Admin admin) {
        adminRepo.save(admin);
    }

    public Optional<Admin> findByEmail(String email) {
        var res =  adminRepo.findByEmail(email);
        log.info("Found admin with email: " + email);
        return res;
    }
}
