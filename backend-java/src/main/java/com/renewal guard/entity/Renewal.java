package com.renewalguard.entity;

import com.renewalguard.enums.DecisionType;
import com.renewalguard.enums.RenewalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Tracks one renewal cycle for a Contract as it moves through the agent
 * pipeline: Planner -> Research -> Decision -> Action -> (human) -> Notification.
 */
@Entity
@Table(name = "renewal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Renewal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RenewalStatus status = RenewalStatus.DETECTED;

    @Enumerated(EnumType.STRING)
    private DecisionType decision;

    /** Market price comparables found by the Research Agent, stored as JSON text. */
    @Column(columnDefinition = "TEXT")
    private String researchSummaryJson;

    /** Why the Decision Agent chose what it chose - shown to the human for trust. */
    @Column(columnDefinition = "TEXT")
    private String decisionRationale;

    /** Draft email/action body written by the Action Agent. */
    @Column(columnDefinition = "TEXT")
    private String draftActionBody;

    private BigDecimal estimatedSavings;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant resolvedAt;
}
