package com.imdtravel.service;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.dto.FlightResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.time.LocalDate;

@Service
public class FlightService {

    private final AirlinesHubClient airlinesHubClient;

    public FlightService(AirlinesHubClient airlinesHubClient) {
        this.airlinesHubClient = airlinesHubClient;
    }

    @Retry(name = "flight-retry")
    public FlightResponse getFlightWithRetry(String flight, LocalDate day) throws SocketTimeoutException {
        return airlinesHubClient.getFlight(flight, day);
    }

    public FlightResponse getFlightNoTolerance(String flight, LocalDate day) throws SocketTimeoutException {
        return airlinesHubClient.getFlight(flight, day);
    }


}
