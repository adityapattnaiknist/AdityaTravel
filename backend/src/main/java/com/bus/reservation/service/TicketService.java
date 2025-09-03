package com.bus.reservation.service;

import com.bus.reservation.entity.Ticket;
import com.bus.reservation.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket save(Ticket ticket) { return ticketRepository.save(ticket); }
    public Optional<Ticket> findById(Long id) { return ticketRepository.findById(id); }
    public List<Ticket> findAll() { return ticketRepository.findAll(); }
    public void deleteById(Long id) { ticketRepository.deleteById(id); }
    public Ticket update(Long id, Ticket updated) { updated.setId(id); return ticketRepository.save(updated); }
}

