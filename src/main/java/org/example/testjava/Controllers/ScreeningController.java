package org.example.testjava.Controllers;

import org.example.testjava.Models.Screening;
import org.example.testjava.Services.ScreeningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/screenings")
public class ScreeningController {

    private static final Logger logger = LoggerFactory.getLogger(ScreeningController.class);
    private final ScreeningService screeningService;

    @Autowired
    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping
    public ResponseEntity<List<Screening>> getAllScreenings() {
        logger.info("GET /screenings called");
        List<Screening> screenings = screeningService.getAllScreenings();
        if (screenings.isEmpty()) {
            logger.warn("No screenings found");
            return ResponseEntity.notFound().build();
        }
        logger.info("Returning {} screenings", screenings.size());
        return ResponseEntity.ok(screenings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Screening> getScreeningById(@PathVariable Long id) {
        logger.info("GET /screenings/{} called", id);
        try {
            Screening screening = screeningService.getScreeningById(id);
            logger.info("Screening found for ID: {}", id);
            return ResponseEntity.ok(screening);
        } catch (Exception e) {
            logger.warn("Screening with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Screening> addScreening(@RequestBody Screening screening) {
        logger.info("POST /screenings called with screening: {}", screening);
        try {
            Screening createdScreening = screeningService.addScreening(screening);
            logger.info("Created screening with ID: {}", createdScreening.getId());
            return ResponseEntity.ok(createdScreening);
        } catch (Exception e) {
            logger.error("Failed to create screening: Movie with ID {} not found", screening.getMovie().getId());
            return ResponseEntity.badRequest().build();
        }
    }
}
