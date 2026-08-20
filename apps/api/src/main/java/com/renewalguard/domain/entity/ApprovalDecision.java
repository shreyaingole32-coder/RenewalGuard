package com.renewalguard.domain.entity;

import com.renewalguard.domain.enums.DecisionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "approval_items", indexes = {
        @Index(name = "idx_approval_items_renewal_id", columnList = "renewal_id"),
        @Index(name = "idx_approval_items_status", columnList = "decision")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "renewal_id", nullable = false)
    private Renewal renewal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "decided_by_user_id")
    private User decidedBy;

    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private DecisionType decision;

    @Column(name = "edited_email_body", columnDefinition = "TEXT")
    private String editedEmailBody;

    @Column(name = "decision_note", columnDefinition = "TEXT")
    private String decisionNote;

    @Column(name = "decided_at")
    private Instant decidedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
