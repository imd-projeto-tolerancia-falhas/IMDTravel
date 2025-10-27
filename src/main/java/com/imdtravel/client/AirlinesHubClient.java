package com.imdtravel.client;

import com.imdtravel.dto.FlightResponse;
import com.imdtravel.dto.SellRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.time.LocalDate;
import java.util.UUID;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface AirlinesHubClient {

    @GetExchange(value = "/flight")
    FlightResponse getFlight(@RequestParam String flight, @RequestParam LocalDate day);

    @PostExchange(value = "/sell")
    UUID sellTicket(@RequestBody SellRequest sellRequest);
}
