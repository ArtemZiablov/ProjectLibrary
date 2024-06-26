package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.configs.StatisticsProperties;
import ua.karazin.interfaces.ProjectLibrary.exceptions.BookAlreadyRegisteredException;
import ua.karazin.interfaces.ProjectLibrary.models.*;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookRepo;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    private final BookCopyService bookCopyService;
    private final BookOperationService bookOperationService;
    private final RegistrationService registrationService;
    private final StatisticsProperties statisticsProperties;

    @Transactional
    public void addBook(Book book, int copiesAmount) {
        if (bookRepo.findByIsbn(book.getIsbn()).isPresent()) {
            throw new BookAlreadyRegisteredException();
        }
        book.setDateOfAdd(new Date());
        bookRepo.save(book);

        assignBookToAuthors(book);
        assignBookToTranslators(book);
        assignBookToGenres(book);
        bookCopyService.addBookCopies(new BookCopy(book, "free"), copiesAmount);
    }

    private void assignBookToAuthors(Book book) {
        for (Author author : book.getAuthors()) {
            author.getBooks().add(book);
        }
    }

    private void assignBookToTranslators(Book book) {
        for (Translator translator : book.getTranslators()) {
            translator.getBooks().add(book);
        }
    }

    private void assignBookToGenres(Book book) {
        for (Genre genre : book.getGenres()) {
            genre.getBooks().add(book);
        }
    }

    public Optional<Book> findBookByIsbn(Long isbn) {
        return bookRepo.findByIsbn(isbn);
    }

    public List<Book> findBooksByTitleStartingWith(String titlePrefix) {
        return bookRepo.findBooksByTitleStartingWith(titlePrefix);
    }


    public List<Book> findBooksByAuthorsNameStartingWith(String authorsPrefix) {
        return bookRepo.findBooksByAuthorsStartingWith(authorsPrefix);
    }

    public List<Book> findBooksByGenreStartingWith(String genrePrefix) {
        return bookRepo.findBooksByGenreStartingWith(genrePrefix);
    }

    public List<Book> getNovelties(Integer amount) {
        return bookRepo.findNovelties(PageRequest.of(0, amount)).getContent();
    }

    public Long countAllBooks() {
        return bookRepo.count();
    }

    public HashMap<String, Integer> getBooksStatistics() {
        HashMap<String, Integer> bookStatistics = new HashMap<>();

        bookStatistics.put(statisticsProperties.books(), countAllBooks().intValue());
        bookStatistics.put(statisticsProperties.copies(), bookCopyService.countAllBookCopies().intValue());
        bookStatistics.put(statisticsProperties.assignedBookCopies(), bookCopyService.countAssignedBookCopies());
        bookStatistics.put(statisticsProperties.owedBooks(), bookOperationService.countOwedBooks());
        bookStatistics.put(statisticsProperties.reservedBooks(), registrationService.reservedBooksCount().intValue());

        return bookStatistics;
    }

    public List<Book> getBooksByGenres(List<String> genres) {
        var books = bookRepo.findBooksByGenreStartingWith(genres.get(0));

        for (int i = 1; i < genres.size(); i++) {
            books = sortByNextGenre(books, genres.get(i));
        }
        return books;
    }

    private List<Book> sortByNextGenre(List<Book> books, String genre) {
        List<Book> sortedBooks = new ArrayList<>();
        for (Book book : books) {
            for (Genre genreBook : book.getGenres()) {
                if (genreBook.getGenreName().toLowerCase().contains(genre.toLowerCase()))
                    sortedBooks.add(book);
            }
        }
        return sortedBooks;
    }

    public List<Book> getSameAuthorsBooks(Long isbn) {
        List<Author> authors = bookRepo.findBookAuthors(isbn);
        List<Book> authorsBooks = new ArrayList<>();

        for (Author author : authors) {
            for (Book book : author.getBooks()) {
                if (!authorsBooks.contains(book) && !book.getIsbn().equals(isbn))
                    authorsBooks.add(book);
            }
        }

        return authorsBooks;
    }

    public List<Book> getSameGenresBooks(Long isbn) {
        List<Genre> genres = bookRepo.findBookGenres(isbn);
        List<Book> genresBooks = new ArrayList<>();

        for (Genre genre : genres) {
            for (Book book : genre.getBooks()) {
                if (!genresBooks.contains(book) && !book.getIsbn().equals(isbn))
                    genresBooks.add(book);
            }
        }

        return genresBooks;
    }
}
