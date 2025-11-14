package com.imdtravel.service;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.dto.FlightResponse;
import com.imdtravel.exceptions.CircuitBreakerIsOpenException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.time.LocalDate;

@Service
public class FlightService1 {

    private final AirlinesHubClient airlinesHubClient;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public FlightService1(AirlinesHubClient airlinesHubClient) {
        this.airlinesHubClient = airlinesHubClient;
    }

    @Retry(name = "flight-retry")
    public FlightResponse getFlightWithRetry(String flight, LocalDate day) throws SocketTimeoutException {
        return airlinesHubClient.getFlight(flight, day);
    }

    public FlightResponse getFlightNoTolerance(String flight, LocalDate day) throws SocketTimeoutException {
        return airlinesHubClient.getFlight(flight, day);
    }


    @CircuitBreaker(name = "flightCircuitBreaker", fallbackMethod = "fallbackGetFlight")
    public FlightResponse getFlightWithTolerance(String flight, LocalDate day) throws SocketTimeoutException {
        log.info("Trying to get flight...");
        return airlinesHubClient.getFlight(flight, day);
    }

    public FlightResponse fallbackGetFlight(String flight, LocalDate day, Throwable t) {
        log.warn("CircuitBreaker is open...");
        throw new CircuitBreakerIsOpenException("AirlinesHub is currently unavailable.");
    }


}
