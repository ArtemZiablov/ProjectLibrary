package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.ReaderRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepo readerRepo;
    private final BookOperationService bookOperationService;
    private final BookReservationService bookReservationService;

    public void save(Reader reader) {
        readerRepo.save(reader);
    }

    public Optional<Reader> findReaderById(int readerId){
        return readerRepo.findById(readerId);
    }

    public Reader findByFullName(String username){
        return readerRepo.findByFullName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Reader with username " + username + " not found"));
    }

    public Optional<List<Reader>> findReadersByFullNameStartingWith(String fullName){
        return readerRepo.findReadersByFullNameStartingWith(fullName);
    }

    public Optional<List<Reader>> findReadersById(int readerId){
        return readerRepo.findReaderById(readerId);
    }

    public Optional<List<Reader>> findReadersByPhoneNumber(String phoneNumber){
        return readerRepo.findReadersByPhoneNumber(phoneNumber);
    }

    public Long countReaders(){
        return readerRepo.count();
    }

    public int countDebtors(){
        return readerRepo.countDebtors();
    }

    public HashMap<String, Integer> getReadersStatistics() {
        HashMap<String, Integer> readersStatistics = new HashMap<>();

        readersStatistics.put("registeredReaders", countReaders().intValue());
        readersStatistics.put("debtors", countDebtors());
        readersStatistics.put("ongoingReaders", bookOperationService.countOngoingReaders());
        readersStatistics.put("readersWhoReservedBooks", bookReservationService.countReadersWhoReservedBooks());

        return readersStatistics;
    }
}
