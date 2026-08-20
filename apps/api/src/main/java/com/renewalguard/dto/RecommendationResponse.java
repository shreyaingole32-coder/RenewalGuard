package com.renewalguard.dto;

import com.renewalguard.domain.enums.RenewalAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record RecommendationResponse(
        UUID id,
        UUID contractId,
        String vendorName,
        RenewalAction recommendedAction,
        BigDecimal estimatedAnnualSavings,
        double confidenceScore,
        String reasoning,
        String draftEmailSubject,
        String draftEmailBody,
        String generatedByModel,
        Instant createdAt
) {}
