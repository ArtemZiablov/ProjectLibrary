package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.models.Librarian;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookReservationRepo;


@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final BookReservationRepo bookReservationRepo;
    private final ReaderService readerService;
    private final LibrarianService librarianService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerReader(Reader reader){
        String encodedPassword = passwordEncoder.encode(reader.getPassword());
        reader.setPassword(encodedPassword);
        reader.setDebtor(false);
        readerService.save(reader);
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
}
