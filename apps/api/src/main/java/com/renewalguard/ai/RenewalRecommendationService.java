package com.renewalguard.ai;

import com.renewalguard.domain.entity.Contract;
import com.renewalguard.domain.entity.Renewal;
import com.renewalguard.repository.ContractRepository;
import com.renewalguard.repository.RenewalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RenewalRecommendationService {

    private final AgentsClient agentsClient;
    private final ContractRepository contractRepository;
    private final RenewalRepository renewalRepository;

    @Transactional
    public Map<String, Object> generate(UUID renewalId) {
        Renewal renewal = renewalRepository.findById(renewalId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Renewal not found: " + renewalId));

        Contract contract = renewal.getContract();

        return agentsClient
                .generateRecommendation(
                        renewal.getId(),
                        contract.getId()
                )
                .blockOptional()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Agents service returned no recommendation"
                        ));
    }

    @Transactional
    public Map<String, Object> process(UUID renewalId) {
        Renewal renewal = renewalRepository.findById(renewalId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Renewal not found: " + renewalId));

        return agentsClient
                .processRenewal(
                        renewal.getId(),
                        renewal.getContract().getId()
                )
                .blockOptional()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Agents service returned no renewal result"
                        ));
    }
}
