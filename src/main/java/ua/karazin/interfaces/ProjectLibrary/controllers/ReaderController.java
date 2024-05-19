package ua.karazin.interfaces.ProjectLibrary.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ua.karazin.interfaces.ProjectLibrary.configs.BookProperties;
import ua.karazin.interfaces.ProjectLibrary.dto.*;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NoRequestedParametersWereProvidedException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.NotAuthenticatedException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReaderNotExistException;
import ua.karazin.interfaces.ProjectLibrary.exceptions.ReadersNotFoundException;
import ua.karazin.interfaces.ProjectLibrary.models.Author;
import ua.karazin.interfaces.ProjectLibrary.models.BookReservation;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.security.LibrarianDetails;
import ua.karazin.interfaces.ProjectLibrary.security.ReaderDetails;
import ua.karazin.interfaces.ProjectLibrary.services.BookCopyService;
import ua.karazin.interfaces.ProjectLibrary.services.BookOperationService;
import ua.karazin.interfaces.ProjectLibrary.services.BookReservationService;
import ua.karazin.interfaces.ProjectLibrary.services.ReaderService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j(topic = "ReaderController")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/reader")
@RestController
public class ReaderController {
    private final ReaderService readerService;
    private final BookCopyService bookCopyService;
    private final BookReservationService bookReservationService;
    private final BookOperationService bookOperationService;
    private final BookProperties bookProperties;

    @GetMapping("/info")
    public ReadersInfoDTO getReadersInfo(@RequestParam("id") Integer id, Authentication authentication) {

        log.info("req to /reader/info?id={}", id);
        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            int readerId = readerDetails.reader().getId();
            if (readerId == id) {
                return getInfo(id);
            } else throw new NotAuthenticatedException();
        } else if (authentication != null && authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            return getInfo(id);
        }

        throw new NotAuthenticatedException();
    }

    private ReadersInfoDTO getInfo(Integer id) {
        return readerService.findReaderById(id).map(reader ->
                new ReadersInfoDTO(
                        reader.getId(),
                        reader.getFullName(),
                        reader.getDateOfBirth(),
                        reader.getPhoneNumber(),
                        reader.getEmail(),
                        reader.isDebtor(),
                        reader.getProfilePhoto(),
                        bookCopyService.getReadersBookCopies(id),
                        bookReservationService.findReadersReservedBooks(id).stream()
                                .map(book -> new GetBookDTO(
                                        book.getIsbn(),
                                        book.getTitle(),
                                        book.getAuthors().stream().
                                                map(Author::getFullName).collect(Collectors.joining(", ")),
                                        book.getGenres().stream().
                                                map(Genre::getGenreName).collect(Collectors.joining(", ")),
                                        book.getBookPhoto()
                                ))
                                .toList()

                )
        ).orElseThrow(ReaderNotExistException::new);
    }


    @GetMapping("/search")
    public SearchedReadersDTO findReaders(@RequestParam("name") Optional<String> name,
                                          @RequestParam("id") Optional<Integer> id,
                                          @RequestParam("phoneNum") Optional<String> phoneNum) {

        Optional<List<Reader>> res;
        if (name.isPresent() && name.get().trim().length() >= 4) {
            log.info("Req to /reader/search?name={}", name);
            res = readerService.findReadersByFullNameStartingWith(name.get());
        } else if (id.isPresent()) {
            log.info("Req to /reader/search?id={}", id);
            res = readerService.findReadersById(id.get());
        } else if (phoneNum.isPresent()) {
            log.info("Req to /reader/search?phoneNum={}", phoneNum);
            res = readerService.findReadersByPhoneNumber(phoneNum.get());
        } else {
            throw new ReadersNotFoundException();
        }

        return mapToSearchedReadersDTO(res);
    }

    @GetMapping("/taken-books")
    public List<ReadersBookCopyDTO> getReadersTakenBooks(@RequestParam("readerId") Optional<Integer> readerId) {
        return bookCopyService.getReadersBookCopies(readerId.orElseThrow(NoRequestedParametersWereProvidedException::new));
    }

    @GetMapping("/reserved-books")
    public List<GetReservedBookDTO> getReadersReservedBooks(@RequestParam("readerId") Optional<Integer> readerId) {
        var reservations = bookReservationService.getReadersReservations(readerId.orElseThrow(NoRequestedParametersWereProvidedException::new));

        return reservations.stream()
                .map(bookReservation -> new GetReservedBookDTO(
                        bookReservation.getBook().getIsbn(),
                        bookReservation.getBook().getTitle(),
                        bookReservation.getBook().getAuthors().stream().
                                map(Author::getFullName).collect(Collectors.joining(", ")),
                        bookReservation.getBook().getGenres().stream().
                                map(Genre::getGenreName).collect(Collectors.joining(", ")),
                        bookReservation.getBook().getBookPhoto(),
                        calculateDaysLeft(bookReservation)
                ))
                .toList();
    }

    private int calculateDaysLeft(BookReservation reservation) {

        if (reservation.getStatus().equals(bookProperties.awaitReservationStatus()))
            return -1;

        Date dateOfReservation = reservation.getDateOfReservation();
        LocalDateTime reservationDateTime = dateOfReservation.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime endOfWorkingDay = reservationDateTime.with(LocalTime.of(19, 0));
        LocalDateTime startOfNextDay = reservationDateTime.with(LocalTime.of(7, 0)).plusDays(1);

        int daysLeft = (int) (3 - ChronoUnit.DAYS.between(reservationDateTime.toLocalDate(), currentDateTime.toLocalDate()));

        if (reservationDateTime.isBefore(startOfNextDay)) {
            daysLeft -= 1;
        } else if (currentDateTime.isAfter(endOfWorkingDay)) {
            daysLeft -= 1;
        }

        if (daysLeft < 0) {
            daysLeft = 0;
        }

        return daysLeft;
    }


    @GetMapping("/history")
    public List<ReadersReturnedBookDTO> getReadersHistory(@RequestParam("readerId") Integer id, Authentication authentication) {
        log.info("Req to /reader/history?readerId={}", id);
        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            int readerId = readerDetails.reader().getId();
            if (readerId == id) {
                return bookOperationService.findReadersReturnedBooks(id);
            } else throw new NotAuthenticatedException();
        } else if (authentication != null && authentication.getPrincipal() instanceof LibrarianDetails librarianDetails) {
            return bookOperationService.findReadersReturnedBooks(id);
        }

        throw new NotAuthenticatedException();

    }

    @GetMapping("/photo")
    public Map<String, String> getPhoto(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof ReaderDetails readerDetails) {
            int readerId = readerDetails.reader().getId();
            Map<String, String> res = new HashMap<>();
            String photo = readerService.getReadersPhoto(readerId);
            res.put("photo", photo);
            log.info("res: {}", res);
            return res;
        } else throw new NotAuthenticatedException();
    }

    protected SearchedReadersDTO mapToSearchedReadersDTO(Optional<List<Reader>> optionalReaders) {
        return optionalReaders.map(readers -> readers.stream()
                        .map(reader -> new SearchReaderDTO(
                                reader.getId(), reader.getFullName(), reader.getPhoneNumber(), reader.getProfilePhoto()
                        )).toList())
                .map(SearchedReadersDTO::new)
                .orElseThrow(ReadersNotFoundException::new);
    }
}
