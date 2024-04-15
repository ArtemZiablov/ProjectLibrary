package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookAlreadyRegisteredException;
import ua.karazin.interfaces.ProjectLibrary.models.*;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookRepo;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    private final BookCopyService bookCopyService;


    @Transactional
    public void addBook(Book book, int copiesAmount) {
        if (bookRepo.findByIsbn(book.getIsbn()).isPresent()){
            throw new BookAlreadyRegisteredException();
        }
        bookRepo.save(book);
        assignBookToAuthors(book);
        assignBookToTranslators(book);
        assignBookToGenres(book);
        bookCopyService.addBookCopies(/*new BookCopy(book.getIsbn(), "free")*/
                new BookCopy(book, "free"), copiesAmount);
    }

    private void assignBookToAuthors(Book book) {
        for(Author author : book.getAuthors()){
            author.getBooks().add(book);
        }
    }

    private void assignBookToTranslators(Book book) {
        for(Translator translator : book.getTranslators()){
            translator.getBooks().add(book);
        }
    }

    private void assignBookToGenres(Book book) {
        for(Genre genre : book.getGenres()){
            genre.getBooks().add(book);
        }
    }

    public Optional<Book> findBookByIsbn(Integer isbn){
        return bookRepo.findByIsbn(isbn);
    }

}
