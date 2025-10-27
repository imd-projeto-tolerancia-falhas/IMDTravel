package com.imdtravel;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.client.ExchangeClient;
import com.imdtravel.client.FidelityClient;
import com.imdtravel.dto.BonusRequest;
import com.imdtravel.dto.BuyRequest;
import com.imdtravel.dto.SellRequest;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class TicketService {
    private final AirlinesHubClient airlinesHubClient;
    private final ExchangeClient exchangeClient;
    private final FidelityClient fidelityClient;

    public TicketService(AirlinesHubClient airlinesHubClient, ExchangeClient exchangeClient, FidelityClient fidelityClient) {
        this.airlinesHubClient = airlinesHubClient;
        this.exchangeClient = exchangeClient;
        this.fidelityClient = fidelityClient;
    }

    public UUID buyTicket(BuyRequest buyRequest) {
        try{
            var flightResponse = airlinesHubClient.getFlight(buyRequest.flight(), buyRequest.day());
            var exchangeRate = exchangeClient.getExchangeRate();
            var sellId = airlinesHubClient.sellTicket(new SellRequest(buyRequest.flight(), buyRequest.day()));
            var ok = fidelityClient.addBonus(new BonusRequest(buyRequest.user(), flightResponse.value().intValue()));
            return sellId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
