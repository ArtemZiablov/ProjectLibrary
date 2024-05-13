package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.models.Admin;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookReservationRepo;

import java.util.List;


@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final BookReservationRepo bookReservationRepo;
    private final ReaderService readerService;
    private final LibrarianService librarianService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerReader(Reader reader){
        String encodedPassword = passwordEncoder.encode(reader.getPassword());
        reader.setPassword(encodedPassword);
        reader.setDebtor(false);
        readerService.save(reader);
    }

    @Transactional
    public void registerMultipleReaders(List<Reader> readers){
        for (Reader reader : readers) {
            String encodedPassword = passwordEncoder.encode(reader.getPassword());
            reader.setPassword(encodedPassword);
            reader.setDebtor(false);
            readerService.save(reader);
        }
    }

    @Transactional
    public void registerMultipleLibrarians(List<Librarian> librarians) {
        for (Librarian librarian : librarians) {
            String encodedPassword = passwordEncoder.encode(librarian.getPassword());
            librarian.setPassword(encodedPassword);
            librarianService.save(librarian);
        }
    }

    @Transactional
    public void registerLibrarian(Librarian librarian){
        String encodedPassword = passwordEncoder.encode(librarian.getPassword());
        librarian.setPassword(encodedPassword);
        librarianService.save(librarian);
    }

    public Long reservedBooksCount(){
        return bookReservationRepo.count();
    }

    @Transactional
    public void registerAdmin(Admin admin) {
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        adminService.save(admin);
    }

}
