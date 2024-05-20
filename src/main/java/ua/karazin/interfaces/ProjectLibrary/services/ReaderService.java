package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.configs.StatisticsProperties;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.ReaderRepo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepo readerRepo;
    private final BookOperationService bookOperationService;
    private final BookReservationService bookReservationService;
    private final StatisticsProperties statisticsProperties;


    public void save(Reader reader) {
        readerRepo.save(reader);
    }

    public Optional<Reader> findReaderById(int readerId) {
        return readerRepo.findById(readerId);
    }

    public Optional<List<Reader>> findReadersByFullNameStartingWith(String fullName) {
        return readerRepo.findReadersByFullNameStartingWith(fullName);
    }

    public Optional<List<Reader>> findReadersById(int readerId) {
        return readerRepo.findReaderById(readerId);
    }

    public Optional<List<Reader>> findReadersByPhoneNumber(String phoneNumber) {
        return readerRepo.findReadersByPhoneNumber(phoneNumber);
    }

    public Optional<Reader> findByFullName(String fullName) {
        return readerRepo.findByFullName(fullName);
    }

    public Optional<Reader> findByEmail(String email) {
        return readerRepo.findByEmail(email);
    }

    public Optional<Reader> findByDateOfBirth(Date birthDate) {
        return readerRepo.findByDateOfBirth(birthDate);
    }

    public Long countReaders() {
        return readerRepo.count();
    }

    public int countDebtors() {
        return readerRepo.countDebtors();
    }

    public HashMap<String, Integer> getReadersStatistics() {
        HashMap<String, Integer> readersStatistics = new HashMap<>();

        readersStatistics.put(statisticsProperties.registeredReaders(), countReaders().intValue());
        readersStatistics.put(statisticsProperties.debtors(), countDebtors());
        readersStatistics.put(statisticsProperties.ongoingReaders(), bookOperationService.countOngoingReaders());
        readersStatistics.put(statisticsProperties.readersWhoReservedBooks(), bookReservationService.countReadersWhoReservedBooks());

        return readersStatistics;
    }

    public List<Reader> getDebtors() {
        return readerRepo.findDebtors();
    }

    public String getReadersPhoto(Integer readerId) {
        return readerRepo.findReadersPhoto(readerId);
    }
}
