package com.renewalguard.service;

import com.renewalguard.domain.entity.Contract;
import com.renewalguard.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RenewalService {

    private static final int LOOKAHEAD_DAYS = 90;

    private final ContractRepository contractRepository;
    private final RecommendationService recommendationService;

    @Transactional(readOnly = true)
    public List<Contract> findUpcomingRenewals() {

        LocalDate today = LocalDate.now();

        return contractRepository
                .findByEndDateBetweenAndAutoRenewsTrue(
                        today,
                        today.plusDays(LOOKAHEAD_DAYS)
                );
    }

    @Transactional
    public void generateRecommendation(UUID contractId) {

        recommendationService.generateForContract(contractId);
    }

    public List<Contract> findRenewingBetween(
            LocalDate from,
            LocalDate to) {

        return contractRepository
                .findByEndDateBetweenAndAutoRenewsTrue(
                        from,
                        to
                );
    }
}
