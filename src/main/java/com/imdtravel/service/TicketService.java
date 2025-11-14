package com.imdtravel.service;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.dto.BonusRequest;
import com.imdtravel.dto.BuyRequest;
import com.imdtravel.dto.SellRequest;
import com.imdtravel.dto.SellResponse;
import com.imdtravel.exceptions.AirlinesHubException;
import com.imdtravel.exceptions.CircuitBreakerIsOpenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final AirlinesHubService airlinesHubService;
    private final ExchangeService exchangeService;
    private final FidelityService fidelityService;

    public TicketService(ExchangeService exchangeService, AirlinesHubService airlinesHubService, FidelityService fidelityService) {
        this.exchangeService = exchangeService;
        this.airlinesHubService = airlinesHubService;
        this.fidelityService = fidelityService;
    }


    public SellResponse buyTicket(BuyRequest buyRequest) {
        if (buyRequest.ft().equals(Boolean.TRUE)){
            try{
                var flightResponse = airlinesHubService.getFlightWithRetry(buyRequest.flight(), buyRequest.day());
                var exchangeRate = exchangeService.getTaxOrAverage();
                var sellId = airlinesHubService.sellTicketWithCircuitBreaker(new SellRequest(buyRequest.flight(), buyRequest.day()));
                fidelityService.addBonus(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
                return sellId;
            }
            catch (CircuitBreakerIsOpenException e) {
                log.error(e.getMessage());
                throw e;
            }
            catch (Exception e) {
                log.error(e.getMessage());
                throw new RuntimeException("Não foi possível concluir a compra do ticket.", e);
            }
        } else {
            try{
                var flightResponse = airlinesHubService.getFlightNoTolerance(buyRequest.flight(), buyRequest.day());
                var exchangeRate = exchangeService.getTaxNoTolerance();
                var sellId = airlinesHubService.sellTicketNoTolerance(new SellRequest(buyRequest.flight(), buyRequest.day()));
                fidelityService.addBonusNotTolerant(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
                return sellId;
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }
    }
}

