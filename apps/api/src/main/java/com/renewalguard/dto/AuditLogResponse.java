package com.renewalguard.dto;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
        UUID id,
        String actorType,
        String actorId,
        String action,
        String entityType,
        String entityId,
        String metadataJson,
        Instant createdAt
) {}
