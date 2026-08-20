package com.renewalguard.scheduler;

import com.renewalguard.domain.entity.Contract;
import com.renewalguard.service.RenewalRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RenewalScannerJob {

    private static final int LOOKAHEAD_DAYS = 90;

    private final RenewalRecommendationService renewalRecommendationService;

    @Scheduled(cron = "0 0 6 * * *")
    public void scanUpcomingRenewals() {

        LocalDate today = LocalDate.now();
        LocalDate horizon = today.plusDays(LOOKAHEAD_DAYS);

        List<Contract> contracts =
                renewalRecommendationService.findContractsRenewingBetween(
                        today,
                        horizon
                );

        log.info(
                "Renewal scanner found {} contracts renewing within {} days",
                contracts.size(),
                LOOKAHEAD_DAYS
        );

        for (Contract contract : contracts) {
            try {
                renewalRecommendationService.generateForContract(
                        contract.getId()
                );
            } catch (Exception ex) {
                log.error(
                        "Failed to generate recommendation for contract {}",
                        contract.getId(),
                        ex
                );
            }
        }
    }
}
