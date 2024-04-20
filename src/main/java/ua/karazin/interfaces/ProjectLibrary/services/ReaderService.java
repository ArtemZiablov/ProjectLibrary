package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Reader;
import ua.karazin.interfaces.ProjectLibrary.repositories.ReaderRepo;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReaderService {
    private final ReaderRepo readerRepo;

    public Optional<Reader> findReaderById(int readerId){
        return readerRepo.findById(readerId);
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

}
