package com.renewalguard.domain.entity;

import com.renewalguard.domain.enums.RenewalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "renewals", indexes = {
        @Index(name = "idx_renewals_contract_id", columnList = "contract_id"),
        @Index(name = "idx_renewals_status", columnList = "status"),
        @Index(name = "idx_renewals_renewal_date", columnList = "renewal_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Renewal {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    @Builder.Default
    private RenewalStatus status = RenewalStatus.UPCOMING;

    @Column(name = "recommended_action", length = 64)
    private String recommendedAction;

    @Column(name = "estimated_annual_savings", precision = 14, scale = 2)
    private BigDecimal estimatedAnnualSavings;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(columnDefinition = "TEXT")
    private String reasoning;

    @Column(name = "sources_json", columnDefinition = "TEXT")
    private String sourcesJson;

    @Column(name = "draft_email_subject", length = 512)
    private String draftEmailSubject;

    @Column(name = "draft_email_body", columnDefinition = "TEXT")
    private String draftEmailBody;

    @Column(name = "generated_by_model", length = 128)
    private String generatedByModel;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
