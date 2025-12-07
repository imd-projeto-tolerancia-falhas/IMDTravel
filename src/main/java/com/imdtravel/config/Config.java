package com.imdtravel.config;

import com.imdtravel.client.AirlinesHubClient;
import com.imdtravel.client.ExchangeClient;
import com.imdtravel.client.FidelityClient;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class Config {
    @Bean
    public AirlinesHubClient airlinesHubClient(RestClient.Builder builder) {

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        requestFactory.setConnectTimeout(2000);
        requestFactory.setReadTimeout(2000);

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
