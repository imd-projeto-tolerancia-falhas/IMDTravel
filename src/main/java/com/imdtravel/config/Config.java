package com.imdtravel.config;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.client.ExchangeClient;
import com.imdtravel.client.FidelityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class Config {
    @Bean
    public AirlinesHubClient airlinesHubClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://airlines-hub:8082")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(AirlinesHubClient.class);
    }
    @Bean
    public ExchangeClient exchangeClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://exchange:8083")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(ExchangeClient.class);
    }

    @Bean
    public FidelityClient fidelityClient(RestClient.Builder builder) {
        RestClient restClient = builder
                .baseUrl("http://fidelity:8081")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(FidelityClient.class);
    }
}
