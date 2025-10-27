package com.imdtravel.client;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.math.BigDecimal;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface ExchangeClient {

    @GetExchange(value = "/exchange")
    BigDecimal getExchangeRate();
}
