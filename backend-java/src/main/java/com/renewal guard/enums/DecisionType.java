package com.renewalguard.enums;

/** What the Decision Agent recommends doing about a renewal. */
public enum DecisionType {
    AUTO_RENEW,      // on-market price, low value - let it renew
    NEGOTIATE,       // above-market or high value - draft a negotiation email
    CANCEL,          // unused / not worth renewing - draft cancellation
    ESCALATE         // agent isn't confident - needs human judgment before any draft
}
