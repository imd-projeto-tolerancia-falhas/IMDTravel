package com.imdtravel;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.client.ExchangeClient;
import com.imdtravel.client.FidelityClient;
import com.imdtravel.dto.BonusRequest;
import com.imdtravel.dto.BuyRequest;
import com.imdtravel.dto.SellRequest;
import com.imdtravel.dto.SellResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final AirlinesHubClient airlinesHubClient;
    private final ExchangeService exchangeService;
    private final FidelityClient fidelityClient;

    public TicketService(AirlinesHubClient airlinesHubClient, ExchangeService exchangeService, FidelityClient fidelityClient) {
        this.airlinesHubClient = airlinesHubClient;
        this.exchangeService = exchangeService;
        this.fidelityClient = fidelityClient;
    }


    public SellResponse buyTicket(BuyRequest buyRequest) {
        if (buyRequest.ft().equals(Boolean.TRUE)){
            try{
                var flightResponse = airlinesHubClient.getFlight(buyRequest.flight(), buyRequest.day());
                var exchangeRate = exchangeService.getTaxOrAverage();
                var sellId = airlinesHubClient.sellTicket(new SellRequest(buyRequest.flight(), buyRequest.day()));
                var ok = fidelityClient.addBonus(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
                return sellId;
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        } else {
            try{
                var flightResponse = airlinesHubClient.getFlight(buyRequest.flight(), buyRequest.day());
                var exchangeRate = exchangeService.getTaxNoTolerance();
                var sellId = airlinesHubClient.sellTicket(new SellRequest(buyRequest.flight(), buyRequest.day()));
                var ok = fidelityClient.addBonus(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
                return sellId;
            } catch (Exception e) {
                log.error(e.getMessage());
                return null;
            }
        }
    }
}

