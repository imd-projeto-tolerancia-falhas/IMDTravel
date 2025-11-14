package com.imdtravel.service;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.dto.FlightResponse;
import com.imdtravel.dto.SellRequest;
import com.imdtravel.dto.SellResponse;
import com.imdtravel.exceptions.AirlinesHubException;
import com.imdtravel.exceptions.CircuitBreakerIsOpenException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Fallback;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.net.SocketTimeoutException;
import java.time.LocalDate;

@Service
public class AirlinesHubService {

    private final AirlinesHubClient airlinesHubClient;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AirlinesHubService(AirlinesHubClient airlinesHubClient) {
        this.airlinesHubClient = airlinesHubClient;
    }

    @Retry(name = "flight-retry")
    public FlightResponse getFlightWithRetry(String flight, LocalDate day) throws SocketTimeoutException {
        return airlinesHubClient.getFlight(flight, day);
    }

    public FlightResponse getFlightNoTolerance(String flight, LocalDate day) throws SocketTimeoutException {
        return airlinesHubClient.getFlight(flight, day);
    }

    @CircuitBreaker(name = "sellTicketCircuitBreaker", fallbackMethod = "fallbackSellTicket")
    public SellResponse sellTicketWithCircuitBreaker(SellRequest sellRequest) throws SocketTimeoutException {
        log.info("Trying to sell ticket...");
        return airlinesHubClient.sellTicket(sellRequest);
    }

    public SellResponse fallbackSellTicket(SellRequest sellRequest, Throwable e) {
        log.warn("AirlinesHub call failed: {}", e.getMessage());
        throw new CircuitBreakerIsOpenException("AirlinesHub is unavailable.");
    }

    public SellResponse sellTicketNoTolerance(SellRequest sellRequest) throws SocketTimeoutException {
        return airlinesHubClient.sellTicket(sellRequest);
    }

}
