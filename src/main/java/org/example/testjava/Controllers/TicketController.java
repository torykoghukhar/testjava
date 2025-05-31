package org.example.testjava.Controllers;

import org.example.testjava.Models.Ticket;
import org.example.testjava.Services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Ticket> buyTicket(@RequestBody Ticket ticket) {
        logger.info("POST /tickets - Attempting to buy ticket for screening ID: {}, customer: {}",
                ticket.getScreening().getId(), ticket.getCustomerName());
        try {
            Ticket boughtTicket = ticketService.buyTicket(ticket);
            logger.info("Ticket bought successfully, ID: {}", boughtTicket.getId());
            return ResponseEntity.ok(boughtTicket);
        } catch (Exception e) {
            logger.error("Failed to buy ticket: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/by-customer")
    public ResponseEntity<List<Ticket>> getTicketsByCustomer(@RequestParam String customerName) {
        logger.info("GET /tickets/by-customer - Fetching tickets for customer: {}", customerName);
        List<Ticket> tickets = ticketService.getTicketsByCustomer(customerName);
        if (tickets.isEmpty()) {
            logger.warn("No tickets found for customer: {}", customerName);
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Found {} ticket(s) for customer: {}", tickets.size(), customerName);
            return ResponseEntity.ok(tickets);
        }
    }
}
