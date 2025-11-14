package com.imdtravel.config;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.client.ExchangeClient;
import com.imdtravel.client.FidelityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class Config {
    @Bean
    public AirlinesHubClient airlinesHubClient(RestClient.Builder builder) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(2));
        requestFactory.setReadTimeout(Duration.ofSeconds(2));

        RestClient restClient = builder
                .baseUrl("http://airlines-hub:8083")
                .requestFactory(requestFactory)
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
                .baseUrl("http://exchange:8082")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(ExchangeClient.class);
    }

    @Bean
    public FidelityClient fidelityClient(RestClient.Builder builder) {
        SimpleClientHttpRequestFactory restFactory = new SimpleClientHttpRequestFactory();
        restFactory.setConnectTimeout(1000);
        restFactory.setReadTimeout(1000);
        RestClient restClient = builder
                .requestFactory(restFactory)
                .baseUrl("http://fidelity:8081")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(FidelityClient.class);
    }
}
