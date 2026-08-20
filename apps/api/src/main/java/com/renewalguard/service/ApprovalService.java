
package com.renewalguard.service;

import com.renewalguard.domain.entity.ApprovalDecision;
import com.renewalguard.domain.entity.RenewalRecommendation;
import com.renewalguard.domain.entity.User;
import com.renewalguard.domain.enums.ApprovalStatus;
import com.renewalguard.dto.DecideApprovalRequest;
import com.renewalguard.repository.ApprovalDecisionRepository;
import com.renewalguard.repository.RenewalRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalDecisionRepository approvalDecisionRepository;
    private final RenewalRecommendationRepository
            recommendationRepository;

    @Transactional
    public ApprovalDecision decide(
            DecideApprovalRequest request,
            User decidingUser) {

        RenewalRecommendation recommendation =
                recommendationRepository
                        .findById(request.recommendationId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Recommendation not found"
                                )
                        );

        ApprovalDecision decision =
                ApprovalDecision.builder()
                        .recommendation(recommendation)
                        .decidedBy(decidingUser)
                        .status(request.status())
                        .editedEmailBody(
                                request.editedEmailBody()
                        )
                        .decisionNote(
                                request.decisionNote()
                        )
                        .decidedAt(Instant.now())
                        .build();

        return approvalDecisionRepository.save(decision);
    }

    public boolean isApproved(ApprovalStatus status) {

        return status == ApprovalStatus.APPROVED ||
                status == ApprovalStatus.EDITED_AND_APPROVED;
    }
}
