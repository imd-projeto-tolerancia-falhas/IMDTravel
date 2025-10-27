package com.imdtravel.client;

import com.imdtravel.dto.BonusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface FidelityClient {

    @PostExchange(value = "/bonus")
    ResponseEntity<Void> addBonus(@RequestBody BonusRequest bonusRequest);
}
