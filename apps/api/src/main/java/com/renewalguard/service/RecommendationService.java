// apps/api/src/main/java/com/renewalguard/service/RecommendationService.java
package com.renewalguard.service;

import com.renewalguard.ai.AgentsClient;
import com.renewalguard.domain.entity.Contract;
import com.renewalguard.domain.entity.RenewalRecommendation;
import com.renewalguard.domain.enums.RenewalAction;
import com.renewalguard.dto.RecommendationResponse;
import com.renewalguard.repository.ContractRepository;
import com.renewalguard.repository.RenewalRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final AgentsClient agentsClient;
    private final ContractRepository contractRepository;
    private final RenewalRecommendationRepository
            recommendationRepository;

    @Transactional
    public RecommendationResponse generateForContract(
            UUID contractId) {

        Contract contract = contractRepository
                .findById(contractId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Contract not found: " + contractId
                        )
                );

        Map<String, Object> result =
                agentsClient
                        .generateRecommendation(contractId)
                        .block();

        if (result == null) {
            throw new IllegalStateException(
                    "Agents service returned no recommendation"
            );
        }

        RenewalRecommendation recommendation =
                RenewalRecommendation.builder()
                        .contract(contract)
                        .recommendedAction(
                                RenewalAction.valueOf(
                                        String.valueOf(
                                                result.get(
                                                        "recommended_action"
                                                )
                                        )
                                )
                        )
                        .estimatedAnnualSavings(
                                new BigDecimal(
                                        String.valueOf(
                                                result.get(
                                                        "estimated_annual_savings"
                                                )
                                        )
                                )
                        )
                        .confidenceScore(
                                Double.parseDouble(
                                        String.valueOf(
                                                result.get(
                                                        "confidence_score"
                                                )
                                        )
                                )
                        )
                        .reasoning(
                                String.valueOf(
                                        result.get("reasoning")
                                )
                        )
                        .sourcesJson(
                                String.valueOf(
                                        result.get("sources")
                                )
                        )
                        .draftEmailSubject(
                                String.valueOf(
                                        result.get(
                                                "draft_email_subject"
                                        )
                                )
                        )
                        .draftEmailBody(
                                String.valueOf(
                                        result.get(
                                                "draft_email_body"
                                        )
                                )
                        )
                        .generatedByModel(
                                String.valueOf(
                                        result.getOrDefault(
                                                "model",
                                                "unknown"
                                        )
                                )
                        )
                        .build();

        RenewalRecommendation saved =
                recommendationRepository.save(recommendation);

        return toResponse(saved);
    }

    private RecommendationResponse toResponse(
            RenewalRecommendation recommendation) {

        return new RecommendationResponse(
                recommendation.getId(),
                recommendation.getContract().getId(),
                recommendation.getContract().getVendorName(),
                recommendation.getRecommendedAction(),
                recommendation.getEstimatedAnnualSavings(),
                recommendation.getConfidenceScore(),
                recommendation.getReasoning(),
                recommendation.getDraftEmailSubject(),
                recommendation.getDraftEmailBody(),
                recommendation.getGeneratedByModel(),
                recommendation.getCreatedAt()
        );
    }
}
