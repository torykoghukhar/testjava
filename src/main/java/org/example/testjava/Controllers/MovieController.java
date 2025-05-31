package org.example.testjava.Controllers;

import org.example.testjava.Models.Movie;
import org.example.testjava.Services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        logger.info("GET /movies called");
        List<Movie> movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            logger.warn("No movies found");
            return ResponseEntity.notFound().build();
        }
        logger.info("Returning {} movies", movies.size());
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        logger.info("GET /movies/{} called", id);
        try {
            Movie movie = movieService.getMovieById(id);
            logger.info("Movie found: {}", movie.getTitle());
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            logger.warn("Movie with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        logger.info("POST /movies called with movie: {}", movie);
        Movie createdMovie = movieService.addMovie(movie);
        logger.info("Created movie with ID: {}", createdMovie.getId());
        return ResponseEntity.ok(createdMovie);
    }
}
