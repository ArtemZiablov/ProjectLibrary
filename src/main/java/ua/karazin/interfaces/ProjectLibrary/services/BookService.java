package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.karazin.interfaces.ProjectLibrary.dto.AuthorDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.BookToAddDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.GenreDTO;
import ua.karazin.interfaces.ProjectLibrary.dto.TranslatorDTO;
import ua.karazin.interfaces.ProjectLibrary.models.*;
import ua.karazin.interfaces.ProjectLibrary.repositories.BookRepo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepo bookRepo;
    private final BookCopyService bookCopyService;
    private final AuthorService authorService;
    //private final BookMapper bookMapper;

    @Transactional
    public void addBook(BookToAddDTO bookToAddDTO) {
        Book book = mapToBook(bookToAddDTO);
        bookRepo.save(book);
        assignBookToAuthors(book);
        bookCopyService.addBookCopies(new BookCopy(book.getIsbn(), "free"), bookToAddDTO.copiesAmount());
    }

    private void assignBookToAuthors(Book book) {
        for(Author author : book.getAuthors()){
            author.getBooks().add(book);
        }
    }

    public Book mapToBook(BookToAddDTO bookToAddDTO) {
        Book book = new Book();
        book.setIsbn(bookToAddDTO.isbn());
        book.setTitle(bookToAddDTO.title());
        book.setYearOfPublishing(bookToAddDTO.yearOfPublishing());
        book.setNumberOfPages(bookToAddDTO.numberOfPages());
        book.setAnnotation(bookToAddDTO.annotation());
        book.setLanguage(bookToAddDTO.language());
        book.setBookPhoto(bookToAddDTO.bookPhoto());
        book.setAuthors(mapToAuthors(bookToAddDTO.authors()));
        book.setTranslators(mapToTranslators(bookToAddDTO.translators()));
        book.setGenres(mapToGenres(bookToAddDTO.genres()));
        return book;
    }

    protected List<Author> mapToAuthors(List<AuthorDTO> authorDTOs) {
        if (authorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return authorDTOs.stream()
                .map(authorDTO -> {
                    var optionalAuthor = authorService.findAuthorByFullNameAndDateOfBirth(
                            authorDTO.fullName(),
                            authorDTO.dateOfBirth());

                    if (optionalAuthor.isPresent()) {
                        return optionalAuthor.get();
                    } else {
                        Author author = new Author();

                        author.setFullName(authorDTO.fullName());
                        author.setNationality(authorDTO.nationality());
                        author.setDateOfBirth(authorDTO.dateOfBirth());
                        author.setBooks(Collections.emptyList()); // ???????

                        authorService.AddAuthor(author);  // TODO

                        return author;
                    }
                })
                .collect(Collectors.toList());
    }

    protected List<Translator> mapToTranslators(List<TranslatorDTO> translatorDTOs) {
        if (translatorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return translatorDTOs.stream()
                .map(translatorDTO -> {
                    Translator translator = new Translator();
                    // Assuming you have methods to set properties of Translator from TranslatorDTO
                    //translator.setName(translatorDTO.getName());
                    // Set other properties
                    return translator;
                })
                .collect(Collectors.toList());
    }

    protected List<Genre> mapToGenres(List<GenreDTO> authorDTOs) {
        if (authorDTOs == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return authorDTOs.stream()
                .map(authorDTO -> {
                    Genre genre = new Genre();
                    // Assuming you have methods to set properties of Author from AuthorDTO
                    //author.setName(authorDTO.getName());
                    // Set other properties
                    return genre;
                })
                .collect(Collectors.toList());
    }

}
