package com.imdtravel.service;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.client.FidelityClient;
import com.imdtravel.dto.BonusRequest;
import com.imdtravel.dto.BuyRequest;
import com.imdtravel.dto.SellRequest;
import com.imdtravel.dto.SellResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final AirlinesHubClient airlinesHubClient;
    private final FlightService flightService;
    private final ExchangeService exchangeService;
    private final FidelityService fidelityService;

    public TicketService(AirlinesHubClient airlinesHubClient, ExchangeService exchangeService, FlightService flightService, FidelityService fidelityService) {
        this.airlinesHubClient = airlinesHubClient;
        this.exchangeService = exchangeService;
        this.flightService = flightService;
        this.fidelityService = fidelityService;
    }


    public SellResponse buyTicket(BuyRequest buyRequest) {
        if (buyRequest.ft().equals(Boolean.TRUE)){
            try{
                var flightResponse = flightService.getFlightWithRetry(buyRequest.flight(), buyRequest.day());
                var exchangeRate = exchangeService.getTaxOrAverage();
                var sellId = airlinesHubClient.sellTicket(new SellRequest(buyRequest.flight(), buyRequest.day()));
                fidelityService.addBonus(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
                return sellId;
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException("Não foi possível concluir a compra do ticket.", e);
            }
        } else {
            try{
                var flightResponse = flightService.getFlightNoTolerance(buyRequest.flight(), buyRequest.day());
                var exchangeRate = exchangeService.getTaxNoTolerance();
                var sellId = airlinesHubClient.sellTicket(new SellRequest(buyRequest.flight(), buyRequest.day()));
                fidelityService.addBonusNotTolerant(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
                return sellId;
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }
    }
}

