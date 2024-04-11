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

import java.util.ArrayList;
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
    private final TranslatorService translatorService;
    private final GenreService genreService;
    //private final BookMapper bookMapper;

    @Transactional
    public void addBook(BookToAddDTO bookToAddDTO) {
        Book book = mapToBook(bookToAddDTO);
        bookRepo.save(book);
        assignBookToAuthors(book);
        assignBookToTranslators(book);
        assignBookToGenres(book);
        bookCopyService.addBookCopies(new BookCopy(book.getIsbn(), "free"), bookToAddDTO.copiesAmount());
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
                        author.setBooks(new ArrayList<>());
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
                    var optionalTranslator = translatorService.findTranslatorByFullName(translatorDTO.fullName());

                    if (optionalTranslator.isPresent()){
                        return optionalTranslator.get();
                    } else {
                        Translator translator = new Translator();

                        translator.setFullName(translatorDTO.fullName());
                        translator.setBooks(new ArrayList<>());
                        translatorService.addTranslator(translator);

                        return translator;
                    }
                })
                .collect(Collectors.toList());
    }

    protected List<Genre> mapToGenres(List<GenreDTO> genreDTOS) {
        if (genreDTOS == null) {
            return Collections.emptyList(); // Вернуть пустой список
        }
        return genreDTOS.stream()
                .map(genreDTO -> {
                    var optionalGenre = genreService.findGenreByName(genreDTO.genreName());

                    if (optionalGenre.isPresent()){
                        return optionalGenre.get();
                    } else {
                        Genre genre = new Genre();

                        genre.setGenreName(genreDTO.genreName());
                        genre.setBooks(new ArrayList<>());
                        genreService.addGenre(genre);

                        return genre;
                    }
                })
                .collect(Collectors.toList());
    }

}
