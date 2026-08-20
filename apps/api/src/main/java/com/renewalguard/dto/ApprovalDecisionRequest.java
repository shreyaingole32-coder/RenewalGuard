package com.renewalguard.dto;

import com.renewalguard.domain.enums.ApprovalStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ApprovalDecisionRequest(
        @NotNull UUID recommendationId,
        @NotNull ApprovalStatus status,
        String editedEmailBody,
        String decisionNote
) {}
