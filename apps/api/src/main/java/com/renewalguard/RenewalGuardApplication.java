
cd /home/claude/renewalguard/apps/api/src/main/java/com/renewalguard/domain/enums && cat > ContractStatus.java << 'EOF'
package com.renewalguard.domain.enums;

/** Lifecycle status of a tracked SaaS contract. */
public enum ContractStatus {
    ACTIVE,
    RENEWAL_UPCOMING,
    NEGOTIATION_IN_PROGRESS,
    PENDING_APPROVAL,
    RENEWED,
    CANCELLED,
    EXPIRED
}
EOF
cat > RenewalAction.java << 'EOF'
package com.renewalguard.domain.enums;

/** The recommended action an AI agent proposes for a contract's renewal. */
public enum RenewalAction {
    RENEW_AS_IS,
    NEGOTIATE_PRICE,
    NEGOTIATE_TERMS,
    DOWNGRADE,
    CANCEL,
    CONSOLIDATE_WITH_OTHER_VENDOR
}

EOF
cat > ApprovalStatus.java << 'EOF'
package com.renewalguard.domain.enums;

/** Human-in-the-loop decision on an AI-generated recommendation. */
public enum ApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED,
    EDITED_AND_APPROVED,
    EXPIRED
}
EOF
cat > UserRole.java << 'EOF'
package com.renewalguard.domain.enums;

/** RBAC roles within an organization (tenant). */
public enum UserRole {
    OWNER,
    PROCUREMENT_ADMIN,
    APPROVER,
    VIEWER
}
EOF
echo done


Check actual direc
