package com.imdtravel.service;

import com.imdtravel.client.FidelityClient;
import com.imdtravel.dto.BonusRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class FidelityService {
    private final FidelityClient fidelityClient;
    private final Queue<BonusRequest> pending = new ConcurrentLinkedQueue<>();

    public FidelityService(FidelityClient fidelityClient) {
        this.fidelityClient = fidelityClient;
    }

    @Async
    public void addBonus(BonusRequest bonusRequest) {
        try {
            fidelityClient.addBonus(bonusRequest);
        } catch (Exception e) {
            pending.add(bonusRequest);
        }
    }

    public void addBonusNotTolerant(BonusRequest bonusRequest) {
        fidelityClient.addBonus(bonusRequest);
    }

    @Scheduled(fixedRate = 5000)
    public void retryPending() {
        while (!pending.isEmpty()) {
            var bonus = pending.peek();
            try {
                fidelityClient.addBonus(bonus);
                pending.poll();
            } catch (Exception e) {
                break;
            }
        }
    }

}
