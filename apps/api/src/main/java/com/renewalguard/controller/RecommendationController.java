package com.renewalguard.controller;

import com.renewalguard.ai.RenewalRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RenewalRecommendationService recommendationService;

    @PostMapping("/renewals/{renewalId}/generate")
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN')")
    public Map<String, Object> generate(
            @PathVariable UUID renewalId
    ) {
        return recommendationService.generate(renewalId);
    }
}
