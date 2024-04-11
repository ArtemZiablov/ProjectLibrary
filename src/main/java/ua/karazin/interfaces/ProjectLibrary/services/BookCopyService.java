package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.models.BookCopy;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookCopyRepo;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepo bookCopyRepo;

    @Transactional
    public void addBookCopies(BookCopy bookCopy, int amount){
        for(int i = 0; i<amount; i++) {
            bookCopyRepo.save(bookCopy);
        }
    }
}
