package com.imdtravel.controller;

import com.imdtravel.dto.BuyRequest;
import com.imdtravel.dto.SellResponse;
import com.imdtravel.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyTicket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<SellResponse> buyTicket(@RequestBody BuyRequest buyRequest) {
        var response = ticketService.buyTicket(buyRequest);
        if (response.id() == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(response);
    }
}
