
package com.renewalguard.dto;

import java.math.BigDecimal;

public record SavingsSummaryResponse(
        BigDecimal totalAnnualContractValue,
        BigDecimal estimatedSavingsAvailable,
        BigDecimal savingsRealizedYtd,
        long contractsTrackedCount,
        long renewalsNext90Days,
        long pendingApprovalsCount
) {}
