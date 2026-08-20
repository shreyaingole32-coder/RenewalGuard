package com.renewalguard.dto;

import com.renewalguard.domain.enums.ContractStatus;
import com.renewalguard.domain.enums.ContractType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractResponse(
        UUID id,
        String vendorName,
        String contractName,
        ContractType contractType,
        BigDecimal annualValue,
        String currency,
        LocalDate startDate,
        LocalDate endDate,
        boolean autoRenews,
        Integer cancellationNoticeDays,
        ContractStatus status,
        UUID ownerId,
        String ownerEmail,
        String documentUrl,
        long daysUntilRenewal
) {}
