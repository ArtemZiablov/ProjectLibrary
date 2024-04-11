package ua.karazin.interfaces.ProjectLibrary.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.karazin.interfaces.ProjectLibrary.models.Genre;
import ua.karazin.interfaces.ProjectLibrary.repositories.GenreRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepo genreRepo;

    public Optional<Genre> findGenreByName(String genreName){
        return genreRepo.findByGenreName(genreName);
    }

    public void addGenre(Genre genre){
        genreRepo.save(genre);
    }
}
