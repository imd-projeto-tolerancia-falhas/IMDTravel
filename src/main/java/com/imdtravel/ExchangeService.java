package com.imdtravel;

import com.imdtravel.client.ExchangeClient;
import com.imdtravel.exceptions.ExchangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ExchangeService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeService.class);

    private final ExchangeClient exchangeClient;

    private static final Queue<BigDecimal> TAX_CACHE = new ConcurrentLinkedQueue<>();
    private static final int MAX_CACHE_SIZE = 10;
    private static final int CALCULATION_SCALE = 8;

    public ExchangeService(ExchangeClient exchangeClient) {
        this.exchangeClient = exchangeClient;
    }

    public BigDecimal getTaxOrAverage() {
        try {
            BigDecimal CONVERT_TAX = exchangeClient.getExchangeRate();
            TAX_CACHE.add(CONVERT_TAX);

            while (TAX_CACHE.size() > MAX_CACHE_SIZE) {
                TAX_CACHE.poll();
            }
            return CONVERT_TAX;
        } catch (Exception e){
            log.error("Erro ao buscar nova taxa. Usando média do cache existente.");
            return getAverageFromCache();
        }
    }

    public BigDecimal getTaxNoTolerance() {
        try {
            BigDecimal CONVERT_TAX = exchangeClient.getExchangeRate();
        } catch (Exception e) {
            log.error("Erro ao buscar taxa de conversão");
            throw new ExchangeException("Erro ao buscar taxa de conversão");
        }
        return null;
    }

    private BigDecimal getAverageFromCache() {
        synchronized (TAX_CACHE) {
            if (TAX_CACHE.isEmpty()) {
                return BigDecimal.valueOf(5.5);
            }
            BigDecimal sum = TAX_CACHE.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal count = new BigDecimal(TAX_CACHE.size());
            return sum.divide(count, CALCULATION_SCALE, RoundingMode.HALF_UP).setScale(2, RoundingMode.DOWN);
        }
    }

}
