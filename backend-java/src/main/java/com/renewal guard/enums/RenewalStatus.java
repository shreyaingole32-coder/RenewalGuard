package com.renewalguard.enums;

/**
 * Lifecycle of a single contract renewal as it moves through the agent pipeline.
 */
public enum RenewalStatus {
    DETECTED,           // Planner Agent found an upcoming renewal
    RESEARCHING,         // Research Agent gathering market pricing
    AUTO_APPROVED,        // Decision Agent handled it with no human needed
    PENDING_HUMAN_REVIEW,  // Decision Agent flagged it - needs a human
    ACTION_DRAFTED,        // Action Agent has drafted the negotiation/cancellation email
    HUMAN_APPROVED,         // Human clicked approve
    HUMAN_REJECTED,          // Human rejected the drafted action
    SENT,                     // Email/action sent
    COMPLETED,                 // Renewal cycle closed out
    FAILED
}
