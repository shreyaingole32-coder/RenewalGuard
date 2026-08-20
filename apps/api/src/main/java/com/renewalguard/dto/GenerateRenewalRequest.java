package com.renewalguard.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GenerateRenewalRequest(
        @NotNull UUID contractId
) {}
