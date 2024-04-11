package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Translator;
import ua.karazin.interfaces.ProjectLibrary.repositories.TranslatorRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranslatorService {
    private final TranslatorRepo translatorRepo;

    public Optional<Translator> findTranslatorByFullName(String fullName){
        return translatorRepo.findByFullName(fullName);
    }

    public void addTranslator(Translator translator){
        translatorRepo.save(translator);
    }
}
