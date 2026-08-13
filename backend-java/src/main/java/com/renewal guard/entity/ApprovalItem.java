package com.renewalguard.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * The human-in-the-loop queue. A Renewal only produces an ApprovalItem when
 * the Decision Agent could NOT auto-approve it - this table is literally
 * "the only things a human ever has to look at."
 */
@Entity
@Table(name = "approval_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "renewal_id", nullable = false, unique = true)
    private Renewal renewal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_to_user_id", nullable = false)
    private User assignedTo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean resolved = false;

    /** true = approved the drafted action, false = rejected, null = pending */
    private Boolean approved;

    private String reviewerNote;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    private Instant resolvedAt;
}
