package com.renewalguard.controller;

import com.renewalguard.dto.SavingsSummaryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @GetMapping("/savings-summary")
    public SavingsSummaryResponse savingsSummary() {
        return new SavingsSummaryResponse(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0L,
                0L,
                0L
        );
    }
}
