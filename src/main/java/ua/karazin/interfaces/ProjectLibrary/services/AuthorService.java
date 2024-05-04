package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.repositories.AuthorRepo;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepo authorRepo;


    public Optional<Author> findAuthorByFullNameAndDateOfBirth(String name, Date dateOfBirth){
        return authorRepo.findByFullNameAndDateOfBirth(name, dateOfBirth);
    }

    public void AddAuthor(Author author){
        authorRepo.save(author);
    }
}
