package com.renewalguard.dto;

import com.renewalguard.domain.enums.ContractType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateContractRequest(
        @NotNull UUID ownerId,
        @NotBlank String vendorName,
        @NotBlank String contractName,
        @NotNull ContractType contractType,
        @PositiveOrZero BigDecimal annualValue,
        String currency,
        LocalDate startDate,
        LocalDate endDate,
        boolean autoRenews,
        Integer cancellationNoticeDays,
        String documentUrl
) {}
