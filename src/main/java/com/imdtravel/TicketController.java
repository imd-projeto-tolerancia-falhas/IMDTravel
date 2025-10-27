package com.imdtravel;

import com.imdtravel.dto.BuyRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

@RestController
@RequestMapping("/buyTicket")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<UUID> buyTicket(@RequestBody BuyRequest buyRequest) {
        UUID id = ticketService.buyTicket(buyRequest);
        if (id == null) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok(id);
    }
}
