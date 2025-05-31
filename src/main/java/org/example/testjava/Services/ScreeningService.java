package org.example.testjava.Services;

import org.example.testjava.Models.Movie;
import org.example.testjava.Models.Screening;
import org.example.testjava.Repositories.ScreeningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ScreeningService {

    private static final Logger logger = LoggerFactory.getLogger(ScreeningService.class);
    private final ScreeningRepository screeningRepository;
    private final MovieService movieService;

    @Autowired
    public ScreeningService(ScreeningRepository screeningRepository, MovieService movieService) {
        this.screeningRepository = screeningRepository;
        this.movieService = movieService;
    }

    public List<Screening> getAllScreenings() {
        logger.debug("Fetching all screenings");
        List<Screening> list = screeningRepository.findAll();
        logger.info("Fetched {} screenings", list.size());
        return list;
    }

    public Screening getScreeningById(Long id) {
        logger.debug("Fetching screening by ID: {}", id);
        return screeningRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Screening with ID {} not found", id);
                    return new RuntimeException("Screening not found");
                });
    }

    public Screening addScreening(Screening screening) {
        logger.debug("Adding screening: {}", screening);
        Movie movie = movieService.getMovieById(screening.getMovie().getId());
        screening.setMovie(movie);
        Screening saved = screeningRepository.save(screening);
        logger.info("Screening saved with ID: {}", saved.getId());
        return saved;
    }
}
