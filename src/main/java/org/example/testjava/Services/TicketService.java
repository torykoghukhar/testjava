package org.example.testjava.Services;

import org.example.testjava.Models.Screening;
import org.example.testjava.Models.Ticket;
import org.example.testjava.Repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final ScreeningService screeningService;

    @Autowired
    public TicketService(ScreeningService screeningService, TicketRepository ticketRepository) {
        this.screeningService = screeningService;
        this.ticketRepository = ticketRepository;
    }

    public Ticket buyTicket(Ticket ticket) {
        logger.debug("Buying ticket for screening ID: {}", ticket.getScreening().getId());
        try {
            Screening screening = screeningService.getScreeningById(ticket.getScreening().getId());
            ticket.setScreening(screening);
            Ticket savedTicket = ticketRepository.save(ticket);
            logger.info("Ticket successfully saved with ID: {}", savedTicket.getId());
            return savedTicket;
        } catch (Exception e) {
            logger.error("Screening not found for ID: {}", ticket.getScreening().getId());
            throw e;
        }
    }

    public List<Ticket> getTicketsByCustomer(String customerName) {
        logger.debug("Fetching tickets for customer: {}", customerName);
        List<Ticket> tickets = ticketRepository.findByCustomerName(customerName);
        logger.info("Fetched {} ticket(s) for customer: {}", tickets.size(), customerName);
        return tickets;
    }
}
