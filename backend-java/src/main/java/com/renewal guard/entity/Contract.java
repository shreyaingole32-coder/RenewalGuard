package com.renewalguard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A single SaaS/vendor contract belonging to a company (owner = the User who uploaded it).
 * This is populated either by manual entry or by parsing an uploaded contract
 * (S3-stored PDF, parsed by the Python agent service, written back here via
 * AgentBridgeService).
 */
@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private String vendorName;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal currentAnnualCost;

    @Column(nullable = false)
    private LocalDate renewalDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean autoRenews = true;

    /** S3 key of the original uploaded contract file, if any. */
    private String sourceDocumentS3Key;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
