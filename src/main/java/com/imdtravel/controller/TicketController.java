package com.imdtravel.controller;

import com.imdtravel.dto.BuyRequest;
import com.imdtravel.dto.SellResponse;
import com.imdtravel.exceptions.AirlinesHubException;
import com.imdtravel.exceptions.CircuitBreakerIsOpenException;
import com.imdtravel.service.TicketService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;

@RestController
@RequestMapping("/buyTicket")
public class TicketController {

    private static final Logger log = LogManager.getLogger(TicketController.class);
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<?> buyTicket(@RequestBody BuyRequest buyRequest) {
        try{
            var response = ticketService.buyTicket(buyRequest);
            if (response.id() == null) {
                return ResponseEntity.internalServerError().build();
            }
            log.info("Ticket sold successfully!");
            return ResponseEntity.ok(response);
        }catch (CircuitBreakerIsOpenException | AirlinesHubException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }
}
