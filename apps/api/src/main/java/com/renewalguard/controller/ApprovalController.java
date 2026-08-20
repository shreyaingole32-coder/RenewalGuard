package com.renewalguard.controller;

import com.renewalguard.domain.entity.ApprovalItem;
import com.renewalguard.dto.DecideApprovalRequest;
import com.renewalguard.service.ApprovalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping("/{approvalId}/decide")
    @PreAuthorize("hasAnyRole('OWNER', 'PROCUREMENT_ADMIN', 'APPROVER')")
    public ApprovalItem decide(
            @PathVariable UUID approvalId,
            @Valid @RequestBody DecideApprovalRequest request
    ) {
        return approvalService.decide(approvalId, request);
    }
}
