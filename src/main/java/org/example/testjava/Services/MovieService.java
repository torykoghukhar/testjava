package org.example.testjava.Services;

import org.example.testjava.Models.Movie;
import org.example.testjava.Repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        logger.debug("Fetching all movies");
        List<Movie> movies = movieRepository.findAll();
        logger.info("Fetched {} movies", movies.size());
        return movies;
    }

    public Movie getMovieById(Long id) {
        logger.debug("Fetching movie with ID: {}", id);
        return movieRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Movie with ID {} not found", id);
                    return new RuntimeException("Movie not found");
                });
    }

    public Movie addMovie(Movie movie) {
        logger.debug("Adding movie: {}", movie);
        Movie savedMovie = movieRepository.save(movie);
        logger.info("Movie added with ID: {}", savedMovie.getId());
        return savedMovie;
    }
}
